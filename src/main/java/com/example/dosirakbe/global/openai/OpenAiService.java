package com.example.dosirakbe.global.openai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OpenAiService {

    @Value("${openai.api-key}")
    private String apiKey;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public String extractReusableContainerData(String prompt) throws Exception {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        // 요청 본문
        JsonNode requestBody = objectMapper.createObjectNode()
                .put("model", "text-davinci-003")
                .put("prompt", "gpt-3.5-turbo")
                .put("max_tokens", 50)
                .put("temperature", 0.5);

        RequestBody body = RequestBody.create(
                objectMapper.writeValueAsString(requestBody),
                MediaType.get("application/json")
        );

        // HTTP 요청 생성
        Request request = new Request.Builder()
                .url(API_URL)
                .header("Authorization", "Bearer " + apiKey)
                .post(body)
                .build();

        System.out.println("Authorization 헤더: Bearer " + apiKey);

        // API 호출
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("OpenAI API 요청 실패: " + response);
            }
            String responseBody = response.body().string();
            JsonNode responseJson = objectMapper.readTree(responseBody);
            return responseJson.get("choices").get(0).get("text").asText().trim();
        }
    }
}
