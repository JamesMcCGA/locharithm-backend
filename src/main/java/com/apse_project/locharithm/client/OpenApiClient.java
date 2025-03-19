package com.apse_project.locharithm.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class OpenApiClient {

    private static final String API_KEY = "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";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    private final HttpClient httpClient;
    private final Gson gson;

    public OpenApiClient() {
        this.httpClient = HttpClient.newBuilder().build();
        this.gson = new Gson();
    }

    public ResponseEntity<String> sendChatMessage(String prompt) throws IOException, InterruptedException {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", "gpt-4");

        JsonArray messages = new JsonArray();
        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content",
                "You are a problem generator for a competitive programming platform. "
                        + "When given a description such as 'A problem to double an integer', "
                        + "you must respond using ONLY plain text (no Markdown, bold, underscores, etc.) "
                        + "and produce EXACTLY the following sections verbatim:\n\n"
                        + "1) Problem Name\n"
                        + "2) Input Constraints\n"
                        + "3) Input Format\n"
                        + "4) Output Format\n"
                        + "5) Sample Input and Sample Output\n"
                        + "6) Test Cases\n\n"
                        + "Under heading (5), use:\n"
                        + "Sample Input 1:\n"
                        + "[some sample value]\n"
                        + "Sample Output 1:\n"
                        + "[some sample value]\n\n"
                        + "Sample Input 2:\n"
                        + "[some sample value]\n"
                        + "Sample Output 2:\n"
                        + "[some sample value]\n\n"
                        + "Under heading (6), use up to five labeled test cases:\n"
                        + "Test Case Input 1:\n"
                        + "...\n"
                        + "Test Case Output 1:\n"
                        + "...\n\n"
                        + "Test Case Input 2:\n"
                        + "...\n"
                        + "Test Case Output 2:\n"
                        + "...\n\n"
                        + "Test Case Input 3:\n"
                        + "...\n"
                        + "Test Case Output 3:\n"
                        + "...\n\n"
                        + "Test Case Input 4:\n"
                        + "...\n"
                        + "Test Case Output 4:\n"
                        + "...\n\n"
                        + "Test Case Input 5:\n"
                        + "...\n"
                        + "Test Case Output 5:\n"
                        + "...\n\n"
                        + "DO NOT add extra labels or headings, do not skip or rename them. "
                        + "Include edge cases in your test cases. Negatives, extremely large, etc, etc. Cover all bases. "
                        + "If you do not understand what is being asked, explicitly say so."
        );
        messages.add(systemMessage);

        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", prompt);
        messages.add(userMessage);

        requestBody.add("messages", messages);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
            JsonArray choices = jsonResponse.getAsJsonArray("choices");
            if (choices != null && !choices.isEmpty()) {
                JsonObject messageObject = choices.get(0).getAsJsonObject().getAsJsonObject("message");
                String content = messageObject.get("content").getAsString();
                return ResponseEntity.ok(content);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("API response did not contain any choices.");
            }
        } else {
            return ResponseEntity.status(response.statusCode())
                    .body("API call failed with status " + response.statusCode() + ": " + response.body());
        }
    }

    public JsonObject parseResponse(String reply) {
        String pNameStart = "1) Problem Name";
        String inConstStart = "2) Input Constraints";
        String inFormatStart = "3) Input Format";
        String outFormatStart = "4) Output Format";
        String sampleIOStart = "5) Sample Input and Sample Output";
        String testStart = "6) Test Cases";

        String problemName = extractSection(reply, pNameStart, inConstStart);
        String inputConstraints = extractSection(reply, inConstStart, inFormatStart);
        String inputFormat = extractSection(reply, inFormatStart, outFormatStart);
        String outputFormat = extractSection(reply, outFormatStart, sampleIOStart);
        String sampleInputOutput = extractSection(reply, sampleIOStart, testStart);
        String testCases = extractSection(reply, testStart, null);

        String sampleInput1 = extractLineAfterLabel(sampleInputOutput, "Sample Input 1:");
        String sampleOutput1 = extractLineAfterLabel(sampleInputOutput, "Sample Output 1:");
        String sampleInput2 = extractLineAfterLabel(sampleInputOutput, "Sample Input 2:");
        String sampleOutput2 = extractLineAfterLabel(sampleInputOutput, "Sample Output 2:");

        JsonObject result = new JsonObject();
        result.addProperty("problemName", problemName);
        result.addProperty("inputConstraints", inputConstraints);
        result.addProperty("inputFormat", inputFormat);
        result.addProperty("outputFormat", outputFormat);
        result.addProperty("sampleInput1", sampleInput1);
        result.addProperty("sampleOutput1", sampleOutput1);
        result.addProperty("sampleInput2", sampleInput2);
        result.addProperty("sampleOutput2", sampleOutput2);

        for (int i = 1; i <= 5; i++) {
            String tcIn = extractLineAfterLabel(testCases, "Test Case Input " + i + ":");
            String tcOut = extractLineAfterLabel(testCases, "Test Case Output " + i + ":");
            result.addProperty("testCaseInput" + i, tcIn);
            result.addProperty("testCaseOutput" + i, tcOut);
        }

        return result;
    }

    private String extractSection(String text, String startMarker, String endMarker) {
        int startIndex = text.indexOf(startMarker);
        if (startIndex == -1) return "";
        startIndex += startMarker.length();

        int endIndex;
        if (endMarker != null) {
            endIndex = text.indexOf(endMarker, startIndex);
            if (endIndex == -1) endIndex = text.length();
        } else {
            endIndex = text.length();
        }
        return text.substring(startIndex, endIndex).trim();
    }

    private String extractLineAfterLabel(String text, String label) {
        int i = text.indexOf(label);
        if (i == -1) return "";
        i += label.length();
        while (i < text.length() && (text.charAt(i) == ' ' || text.charAt(i) == '\n' || text.charAt(i) == '\r')) {
            i++;
        }
        int nb = text.indexOf("\n", i);
        if (nb == -1) nb = text.length();
        return text.substring(i, nb).trim();
    }

    public static void main(String[] args) {
        OpenApiClient client = new OpenApiClient();
        try {
            ResponseEntity<String> apiResponse = client.sendChatMessage("A problem to double an integer.");
            String reply = apiResponse.getBody();
            JsonObject parsedResponse = client.parseResponse(reply);
            System.out.println(parsedResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
