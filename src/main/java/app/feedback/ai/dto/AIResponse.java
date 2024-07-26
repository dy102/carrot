package app.feedback.ai.dto;

import app.feedback.ai.AIChat;

public record AIResponse(
        String message
) {
    public static AIResponse of(AIChat aiChat) {
        return new AIResponse(aiChat.getChat());
    }
}
