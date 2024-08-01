package app.feedback.ai;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AIService {
    private final ChatClient chatClient;
    private final OllamaChatModel chatModel;
    private final AIRepository aiRepository;
    private static final String DEFAULT_VALUE = "사용자의 글에 대해 칭찬 또는 위로를 해줘.";
    private static final String KOREAN_REQUEST = "Response using only Korean.";

    public AIChat getAIResponse(String message) {
//        String aiResponse = chatClient.prompt().user(DEFAULT_VALUE + message).call().content();
        String call = chatModel.call(message);
        AIChat aiChat = new AIChat(call);
        aiRepository.save(aiChat);
        return aiChat;
    }

    public void initializeAIChat(AIChat aiChat) {
        aiRepository.delete(aiChat);
    }
}
