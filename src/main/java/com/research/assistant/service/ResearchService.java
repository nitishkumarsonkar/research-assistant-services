package com.research.assistant.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.research.assistant.GeminiResponse;
import com.research.assistant.ResearchRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResearchService {

    @Value("${gemini.api.key}")
    String geminiAPIKey;
    @Value("${gemini.api.url}")
    String geminiAPIURL;

    private final WebClient webClient;

    public ResearchService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    // Process content
    public String processContent(ResearchRequest researchRequest) {
        // build the prompt
        String prompt = buildPrompt(researchRequest);

        // creating request body for the API
//        {
//            "contents": [
//            {
//                "parts": [
//                {
//                    "text": "Explain how AI works"
//                }
//                ]
//            }
//        ]
//        }
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of(
                                                "text", prompt
                                        )
                                )
                        )
                )
        );

        //calling gemini API
        String responseBody = webClient.post()
                .uri(geminiAPIURL+geminiAPIKey)
                .bodyValue(requestBody)
                .retrieve().bodyToMono(String.class)
                .block();
        return extractTextFromResponseBody(responseBody);

    }

    // Extract text from response body
    private String extractTextFromResponseBody(String responseBody) {
        try {
            // Extract the text from the response body
            GeminiResponse geminiResponse = new ObjectMapper().readValue(responseBody, GeminiResponse.class);
            if(geminiResponse.getCandidates() != null && !geminiResponse.getCandidates().isEmpty()) {
                return geminiResponse.getCandidates().get(0).getContent().getParts().get(0).getText();
            }
            return "No text from response body";
        }
        catch (Exception e) {
            return "Error extracting text from response body: " + e.getMessage();
        }
    }

    // Build the prompt
    private String buildPrompt(ResearchRequest researchRequest) {
       try {
            String prompt = "";
            if (researchRequest.getOperation().equals("summarize")) {
                prompt = "Summarize the following text: " + researchRequest.getContent();
            } else if (researchRequest.getOperation().equals("generate")) {
                prompt = "Generate a text based on the following text: " + researchRequest.getContent();
            }
            return prompt;
        }
        catch (Exception e) {
            return "Error building prompt witt content:" + e.getMessage();
        }
    }

    // Call the Gemini API
    private String callGeminiAPI(String prompt) {
        try {
            // Call the Gemini API
            return "Response from Gemini API";
        }
        catch (Exception e) {
            return "Error calling Gemini API: " + e.getMessage();
        }
    }
}
