package com.example.dosirakbe.global.openai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.stereotype.Service;


import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
public class OpenAiService {
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "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";

    private final Map<String, String> responseCache = new ConcurrentHashMap<>();

    public String extractReusableContainerData(String dishName) throws Exception {
        // 캐싱된 데이터 확인
        return responseCache.computeIfAbsent(dishName, key -> {
            try {
                return callOpenAiApi(key); // API 호출 및 결과 반환
            } catch (Exception e) {
                throw new RuntimeException("OpenAI API 호출 실패: " + e.getMessage());
            }
        });
    }

    private String callOpenAiApi(String dishName) throws Exception {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper objectMapper = new ObjectMapper();

        String prompt = String.format("%s 1인분을 포장하는 데 필요한 다회용기의 용량을 정확히 숫자와 단위만으로 응답해주세요. 다른 텍스트를 포함하지 마세요. 예: '500ml'.", dishName);
        JsonNode requestBody = objectMapper.createObjectNode()
                .put("model", "gpt-3.5-turbo")
                .put("max_tokens", 50)
                .put("temperature", 0.5)
                .set("messages", objectMapper.createArrayNode()
                        .add(objectMapper.createObjectNode()
                                .put("role", "user")
                                .put("content", prompt)));

        RequestBody body = RequestBody.create(
                objectMapper.writeValueAsString(requestBody),
                MediaType.get("application/json")
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .post(body)
                .build();

        System.out.println("Request URL: " + request.url());
        System.out.println("Request Headers: " + request.headers());
        System.out.println("Request Body: " + objectMapper.writeValueAsString(requestBody));

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException("OpenAI API 요청 실패: " + response.code() + " - " + response.body().string());
            }
            String responseBody = response.body().string();
            JsonNode responseJson = objectMapper.readTree(responseBody);
            return responseJson.get("choices").get(0).get("message").get("content").asText().trim();
        }
    }
}
