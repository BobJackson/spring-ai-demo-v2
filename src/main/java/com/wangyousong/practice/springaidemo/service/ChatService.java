package com.wangyousong.practice.springaidemo.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ChatService {

    @Value("${spring.ai.chat.chunk-size}")
    private int chunkSize;

    private final ChatClient chatClient;

    public ChatService(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel)
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
                .transform(this::toJsonChunk);
    }

    private Flux<String> toJsonChunk(Flux<String> tokenFlux) {
        return Flux.create(sink -> {
            StringBuilder buffer = new StringBuilder();
            tokenFlux.subscribe(
                    token -> {
                        buffer.append(token);
                        int idx;
                        if ((idx = buffer.indexOf("\n")) >= 0) {
                            String line = buffer.substring(0, idx);
                            sink.next(line);
                            buffer.delete(0, idx + 1);
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

    public Mono<String> blockingChat(String prompt) {
        return chatClient.prompt()
                .user(userMessage -> userMessage.text(prompt))
                .stream()
                .content()
                .collectList()
                .map(list -> String.join("", list));
    }
}