package com.tinubu.policies.application.api;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApiErrorResponse {
    private final String type;
    private final String title;
    private final int status;
    private final String detail;
    private final String instance;
    private final LocalDateTime timestamp;

    public ApiErrorResponse(String type, String title, int status, String detail, String instance) {
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.instance = instance;
        this.timestamp = LocalDateTime.now();
    }

}
