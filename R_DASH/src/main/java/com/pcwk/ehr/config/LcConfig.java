package com.pcwk.ehr.config;

import static java.time.Duration.ofSeconds;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

@Configuration
public class LcConfig {

    @Bean
    public ChatLanguageModel openAiChatModel(
            @Value("${openai.apiKey:}") String keyProp,
            @Value("${openai.model:gpt-5-nano}") String model,
            @Value("${openai.temperature:0.7}") double temp,
            @Value("${openai.maxTokens:800}") int maxTokens
    ) {
        String key = (keyProp != null && !keyProp.trim().isEmpty())
                ? keyProp
                : System.getenv("OPENAI_API_KEY");

        return OpenAiChatModel.builder()
                .apiKey(key)
                .modelName(model)
                .temperature(temp)
                .maxTokens(maxTokens)
                .timeout(ofSeconds(30))
                .build();
    }
}