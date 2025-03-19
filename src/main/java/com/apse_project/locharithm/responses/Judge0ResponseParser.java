package com.apse_project.locharithm.responses;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class Judge0ResponseParser {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String retrieveItemFromJsonBody(String body, String targetPath) {
        try {

            JsonNode jsonNode = objectMapper.readTree(body);
            String[] keys = targetPath.split("\\.");
            for (String key : keys) {
                jsonNode = jsonNode.get(key);
                if (jsonNode == null) {
                    return null;
                }
            }
            return jsonNode.asText();
        } catch (JsonProcessingException ex) {
            System.out.println("Json processing exception: " + ex);
        }
        return body;
    }

}
