package com.wangyousong.practice.springaidemo.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChatRequest {
    @NotNull
    private String prompt;
}