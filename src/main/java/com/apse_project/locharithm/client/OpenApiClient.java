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

    private static final String API_KEY = "sk-6601348066e24716b75f039e8aece0b2";
    private static final String API_URL = "https://api.deepseek.com/v1/chat/completions";

    private final HttpClient httpClient;
    private final Gson gson;

    public OpenApiClient() {
        this.httpClient = HttpClient.newBuilder().build();
        this.gson = new Gson();
    }

    public ResponseEntity<String> sendChatMessage(String prompt) throws IOException, InterruptedException {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", "deepseek-chat");
        
        JsonArray messages = new JsonArray();
        
        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content",
        "You are a problem generator for a competitive programming platform. "
        + "You must respond using ONLY plain text, and produce EXACTLY these sections:\n\n"
        + "1) Problem Name\n"
        + "2) Input Constraints\n"
        + "3) Input Format\n"
        + "4) Output Format\n"
        + "5) Sample Input and Sample Output\n"
        + "6) Test Cases\n\n"
        + "When providing sample inputs/outputs (heading 5) and test cases (heading 6), you must use this format:\n"
        + "Sample Input 1:\n"
        + "(multi-line if needed)\n"
        + "<<<END>>>\n"
        + "Sample Output 1:\n"
        + "(multi-line if needed)\n"
        + "<<<END>>>\n\n"
        + "Sample Input 2:\n"
        + "(multi-line if needed)\n"
        + "<<<END>>>\n"
        + "Sample Output 2:\n"
        + "(multi-line if needed)\n"
        + "<<<END>>>\n\n"
        + "Under '6) Test Cases', you must produce up to five test case inputs and outputs, labeled exactly:\n"
        + "Test Case Input 1:\n"
        + "(multi-line if needed)\n"
        + "<<<END>>>\n"
        + "Test Case Output 1:\n"
        + "(multi-line if needed)\n"
        + "<<<END>>>\n\n"
        + "Test Case Input 2:\n"
        + "(multi-line)\n"
        + "<<<END>>>\n"
        + "Test Case Output 2:\n"
        + "(multi-line)\n"
        + "<<<END>>>\n\n"
        + "Test Case Input 3:\n"
        + "(multi-line)\n"
        + "<<<END>>>\n"
        + "Test Case Output 3:\n"
        + "(multi-line)\n"
        + "<<<END>>>\n\n"
        + "Test Case Input 4:\n"
        + "(multi-line)\n"
        + "<<<END>>>\n"
        + "Test Case Output 4:\n"
        + "(multi-line)\n"
        + "<<<END>>>\n\n"
        + "Test Case Input 5:\n"
        + "(multi-line)\n"
        + "<<<END>>>\n"
        + "Test Case Output 5:\n"
        + "(multi-line)\n"
        + "<<<END>>>\n\n"
        + "Do NOT skip or rename these sections and labels. If you do not understand, explicitly say so."
        );
        messages.add(systemMessage);
        
        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", prompt);
        messages.add(userMessage);
        
        requestBody.add("messages", messages);
        
        requestBody.addProperty("stream", false);
        HttpRequest httpRequest = HttpRequest.newBuilder()
        .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(requestBody)))
                .build();

        System.out.println("Request body: " + gson.toJson(requestBody));

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
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
        JsonObject result = new JsonObject();
        result.addProperty("reply", reply);
        String problemName = extractSection(reply, "1) Problem Name", "2) Input Constraints");
        String inputConstraints = extractSection(reply, "2) Input Constraints", "3) Input Format");
        String inputFormat = extractSection(reply, "3) Input Format", "4) Output Format");
        String outputFormat = extractSection(reply, "4) Output Format", "5) Sample Input and Sample Output");
        String sampleIO = extractSection(reply, "5) Sample Input and Sample Output", "6) Test Cases");
        String testCases = extractSection(reply, "6) Test Cases", null);

        result.addProperty("problemName", sanitizeForJson(problemName));
        result.addProperty("inputConstraints", sanitizeForJson(inputConstraints));
        result.addProperty("inputFormat", sanitizeForJson(inputFormat));
        result.addProperty("outputFormat", sanitizeForJson(outputFormat));

        String sampleInput1 = extractBlock(sampleIO, "Sample Input 1:");
        String sampleOutput1 = extractBlock(sampleIO, "Sample Output 1:");
        String sampleInput2 = extractBlock(sampleIO, "Sample Input 2:");
        String sampleOutput2 = extractBlock(sampleIO, "Sample Output 2:");

        result.addProperty("sampleInput1", sanitizeForJson(sampleInput1));
        result.addProperty("sampleOutput1", sanitizeForJson(sampleOutput1));
        result.addProperty("sampleInput2", sanitizeForJson(sampleInput2));
        result.addProperty("sampleOutput2", sanitizeForJson(sampleOutput2));


        for (int i = 1; i <= 5; i++) {
            String tcIn = extractBlock(testCases, "Test Case Input " + i + ":");
            String tcOut = extractBlock(testCases, "Test Case Output " + i + ":");
            result.addProperty("testCaseInput" + i, sanitizeForJson(tcIn));
            result.addProperty("testCaseOutput" + i, sanitizeForJson(tcOut));
        }

        return result;
    }

    /**
     * Replaces all actual newline characters with the literal "\n" so the resulting JSON
     * will contain double-escaped newlines. Also removes carriage returns if present.
     */
    private String sanitizeForJson(String text) {
        if (text == null) return "";
        return text
                .replace("\r\n", "\n")
                .replace("\r", "")
                .replace("\n", "\\n");
    }

    private String extractSection(String text, String startMarker, String endMarker) {
        int startIndex = text.indexOf(startMarker);
        if (startIndex == -1) return "";
        startIndex += startMarker.length();
        int endIndex = (endMarker == null) ? text.length() : text.indexOf(endMarker, startIndex);
        if (endIndex == -1) endIndex = text.length();
        return text.substring(startIndex, endIndex).trim();
    }

    /**
     * Extracts multi-line text from label up to the first occurrence of '<<<END>>>'.
     */
    private String extractBlock(String fullText, String label) {
        int startIndex = fullText.indexOf(label);
        if (startIndex == -1) {
            return "";
        }
        startIndex += label.length();

        int endMarkerIndex = fullText.indexOf("<<<END>>>", startIndex);
        if (endMarkerIndex == -1) {
            return fullText.substring(startIndex).trim();
        }
        return fullText.substring(startIndex, endMarkerIndex).trim();
    }

    public static void main(String[] args) {
        OpenApiClient client = new OpenApiClient();
        try {
            String prompt = "You get 2 strings, output the longer one of the two. If they are the same length, output either.";
            ResponseEntity<String> apiResponse = client.sendChatMessage(prompt);

            if (apiResponse.getStatusCode().is2xxSuccessful()) {
                String reply = apiResponse.getBody();
                JsonObject parsedResponse = client.parseResponse(reply);
                System.out.println("=== Raw reply ===");
                System.out.println(reply);
                System.out.println("=== Parsed JSON ===");
                System.out.println(parsedResponse);
            } else {
                System.err.println("Error: " + apiResponse.getBody());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
