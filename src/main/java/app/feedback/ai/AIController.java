package app.feedback.ai;


import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/ai")
@RestController
@RequiredArgsConstructor
public class AIController {

    private final ChatClient chatClient;

    @GetMapping("/chat")
    public Map<String, String> completion(@RequestParam(defaultValue = "Praise my daily routine.") String message) {
        return Map.of("completion", chatClient.prompt().user(message).call().content());
    }
}