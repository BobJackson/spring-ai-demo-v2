package com.wangyousong.practice.springaidemo.controller;

import com.wangyousong.practice.springaidemo.request.ChatRequest;
import com.wangyousong.practice.springaidemo.service.ChatService;
import com.wangyousong.practice.springaidemo.service.ChatWithMemoryService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@Validated
public class ChatController {
    private final ChatService chatService;
    private final ChatWithMemoryService chatWithMemoryService;

    public ChatController(ChatService chatService, ChatWithMemoryService chatWithMemoryService) {
        this.chatService = chatService;
        this.chatWithMemoryService = chatWithMemoryService;
    }

    @PostMapping(value = "/chat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(@RequestBody @Valid ChatRequest request) {
        return chatService.chat(request.getPrompt());
    }

    @PostMapping(value = "/blockingChat")
    public Mono<String> blockingChat(@RequestBody @Valid ChatRequest request) {
        return chatService.blockingChat(request.getPrompt());
    }

    @PostMapping(value = "/chatWithMemory", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatWithMemory(@RequestBody @Valid ChatRequest request) {
        return chatWithMemoryService.chat(request.getPrompt());
    }
}
