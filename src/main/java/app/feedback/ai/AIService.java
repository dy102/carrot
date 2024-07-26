package app.feedback.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AIService {
    private final ChatClient chatClient;
    private final AIRepository aiRepository;
    private static final String DEFAULT_VALUE = "Praise my daily routine using only Korean.";

    public AIChat getAIResponse(String message) {
        String aiResponse = chatClient.prompt().user(DEFAULT_VALUE + message).call().content();
        AIChat aiChat = new AIChat(aiResponse);
        aiRepository.save(aiChat);
        return aiChat;
    }
}
