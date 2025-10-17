package com.wangyousong.practice.springaidemo.service;

import com.wangyousong.practice.springaidemo.util.ChunkUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatWithMemoryService {

    private final ChatClient chatClient;

    public ChatWithMemoryService(ChatModel chatModel, ChatMemory chatMemory) {
        this.chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    public Flux<String> chat(String prompt) {
        return chatClient.prompt()
                .system(systemMessage -> systemMessage.text(
                        """
                                  Respond in NDJSON format.
                                  Each JSON object should contains around 100 characters.
                                  Sample json object format: {"part":0,"text":"Once in a small town..."}
                                """))
                .user(userMessage -> userMessage.text(prompt))
                .stream()
                .content()
                .transform(ChunkUtils::toJsonChunk);
    }

}
