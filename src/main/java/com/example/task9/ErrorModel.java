package com.example.task9;

import lombok.Data;

@Data
public class ErrorModel {
    private String fields;
    private String message;

    public ErrorModel(String fields, String message) {
        this.fields = fields;
        this.message = message;
    }
}
