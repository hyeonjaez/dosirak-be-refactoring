package com.example.dosirakbe.global.openai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.springframework.stereotype.Service;


import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * packageName    : com.example.dosirakbe.global.openai<br>
 * fileName       : OpenAiService<br>
 * author         : femmefatalehaein<br>
 * date           : 11/29/24<br>
 * description    : OpenAI API와 통신하여 데이터를 처리하는 서비스 클래스입니다.<br>
 * ===========================================================<br>
 * DATE              AUTHOR             NOTE<br>
 * -----------------------------------------------------------<br>
 * 11/29/24        femmefatalehaein                최초 생성<br>
 */
@Service
public class OpenAiService {
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = "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";

    private final Map<String, String> responseCache = new ConcurrentHashMap<>();

    /**
     * 주어진 요리 이름에 대해 다회용기 용량 데이터를 추출합니다.
     *
     * <p>
     * API 호출 결과를 캐싱하여 동일한 요청에 대해 재사용할 수 있도록 최적화되었습니다.
     * </p>
     *
     * @param dishName 요리 이름
     * @return 다회용기 용량을 나타내는 문자열 (예: "500ml")
     * @throws Exception OpenAI API 호출 중 예외가 발생한 경우
     */
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

    /**
     * OpenAI API를 호출하여 주어진 요리 이름에 대한 다회용기 용량을 요청합니다.
     *
     * @param dishName 요리 이름
     * @return 다회용기 용량을 나타내는 문자열 (예: "500ml")
     * @throws Exception API 요청 실패 또는 응답 처리 중 오류가 발생한 경우
     */
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
