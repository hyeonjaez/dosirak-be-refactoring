package com.example.dosirakbe.domain.test.controller;

import com.example.dosirakbe.domain.test.controller.openai.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/containers")
public class OpenAIController {

    @Autowired
    private OpenAiService openAiService;

    public OpenAIController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @PostMapping("/extract")
    public String extractReusableContainerData(@RequestBody String prompt) {
        try {
            return openAiService.extractReusableContainerData(prompt);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

}
