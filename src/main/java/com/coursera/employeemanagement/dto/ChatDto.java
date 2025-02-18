package com.coursera.employeemanagement.dto;

import lombok.Data;

@Data
public class ChatDto {
    private String model;
    private boolean stream;
    private String prompt;
}
