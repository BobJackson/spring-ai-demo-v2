package com.wangyousong.practice.springaidemo.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class ChatService {
    private final ChatClient chatClient;

    public ChatService(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel)
                .build();
    }

    public Flux<String> chat(String prompt) {
        return chatClient.prompt()
                .user(userMessage -> userMessage.text(prompt))
                .stream()
                .content()
                .transform(flux -> toChunk(flux, 100));
    }

    private Flux<String> toChunk(Flux<String> tokenFlux, int chunkSize) {
        return Flux.create(sink -> {
            StringBuilder buffer = new StringBuilder();
            tokenFlux.subscribe(
                    token -> {
                        buffer.append(token);
                        if (buffer.length() >= chunkSize) {
                            sink.next(buffer.toString());
                            buffer.setLength(0);
                        }
                    },
                    sink::error,
                    () -> {
                        if (!buffer.isEmpty()) {
                            sink.next(buffer.toString());
                        }
                        sink.complete();
                    }
            );
        });
    }
}