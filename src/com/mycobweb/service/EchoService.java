package com.mycobweb.service;

public class EchoService {
    public String echo(String input) {
        if (input == null || input.isBlank()) {
            return "(empty)";
        }
        // Business rule: maybe sanitize or validate input
        return input.strip();
    }
}