package com.wangyousong.practice.springaidemo.request;

import lombok.Data;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class ChatRequest {
    @NotNull
    private String prompt;

    private String conversationId;

    public String findConversationId() {
        return StringUtils.hasText(conversationId) ? conversationId : UUID.randomUUID().toString();
    }
}