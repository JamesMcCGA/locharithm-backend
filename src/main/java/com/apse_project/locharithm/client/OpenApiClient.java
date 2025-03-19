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

public class OpenApiClient {

    private static final String API_KEY = "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";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    private final HttpClient httpClient;
    private final Gson gson;

    public OpenApiClient() {
        this.httpClient = HttpClient.newBuilder().build();
        this.gson = new Gson();
    }

    /**
     * Sends a chat message to the OpenAI API and returns the response.
     * The API call includes a system message to prime the context, followed by the user prompt.
     *
     * @param prompt The user's prompt.
     * @return The generated response from the API.
     * @throws IOException if an I/O error occurs.
     * @throws InterruptedException if the operation is interrupted.
     */
    public ResponseEntity<String> sendChatMessage(String prompt) throws IOException, InterruptedException {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", "gpt-4o");

        JsonArray messages = new JsonArray();
        JsonObject systemMessage = new JsonObject();
        systemMessage.addProperty("role", "system");
        systemMessage.addProperty("content", "You are a problem generator for a competitive programming platform. "
                + "When given a brief problem description (e.g., 'A problem to double an integer'), "
                + "generate a complete problem statement that includes the following sections:\n"
                + "# Problem Name \n"
                + "# Input Constraints \n"
                + "# Input Format\n"
                + "# Output Format\n"
                + "# Sample Input and Sample Output\n"
                + "# Test Cases\n"
                + "Always use these headings exactly as written. If you do not understand what is being asked, explicitly say so.");
        messages.add(systemMessage);

        // User message with the provided prompt.
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

    /**
     * Parses the API's reply to extract the problem sections based on Markdown-style headings.
     *
     * @param reply The full text reply from the API.
     * @return A JsonObject containing the parsed sections.
     */
    private JsonObject parseResponse(String reply) {
        JsonObject result = new JsonObject();
        String problemNameStart = "# Problem Name";
        String inputConstraintsStart = "# Input Constraints";
        String inputFormatStart = "# Input Format";
        String outputFormatStart = "# Output Format";
        String sampleIOStart = "# Sample Input and Sample Output";
        String testCasesStart = "# Test Cases";

        String problemName = extractSection(reply, problemNameStart, inputConstraintsStart);
        String inputConstraints = extractSection(reply, inputConstraintsStart, inputFormatStart);
        String inputFormat = extractSection(reply, inputFormatStart, outputFormatStart);
        String outputFormat = extractSection(reply, outputFormatStart, sampleIOStart);
        String sampleInputOutput = extractSection(reply, sampleIOStart, testCasesStart);
        String testCases = extractSection(reply, testCasesStart, null);

        result.addProperty("problem_name", problemName);
        result.addProperty("input_constraints", inputConstraints);
        result.addProperty("input_format", inputFormat);
        result.addProperty("output_format", outputFormat);
        result.addProperty("sample_input_output", sampleInputOutput);
        result.addProperty("test_cases", testCases);
        return result;
    }

    /**
     * Helper method to extract text between a start and an end marker.
     * If endMarker is null, extracts to the end of the text.
     *
     * @param text The full text.
     * @param startMarker The beginning marker.
     * @param endMarker The ending marker (or null to go to the end).
     * @return The extracted section, trimmed of whitespace.
     */
    private String extractSection(String text, String startMarker, String endMarker) {
        int startIndex = text.indexOf(startMarker);
        if (startIndex == -1) {
            return "";
        }
        startIndex += startMarker.length();
        int endIndex;
        if (endMarker != null) {
            endIndex = text.indexOf(endMarker, startIndex);
            if (endIndex == -1) {
                endIndex = text.length();
            }
        } else {
            endIndex = text.length();
        }
        return text.substring(startIndex, endIndex).trim();
    }

    public static void main(String[] args) {
        OpenApiClient client = new OpenApiClient();
        try {
            String reply = String.valueOf(client.sendChatMessage("A problem to double an integer."));
            System.out.println("Full ChatGPT response:\n" + reply + "\n");

            JsonObject parsedResponse = client.parseResponse(reply);
            System.out.println("Parsed JSON response:");
            System.out.println(client.gson.toJson(parsedResponse));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
