// repository/MessageRepository.java
package com.mycobweb.repository;

public class MessageRepository {
    public String getMessage() {
        return "Hello from REST!";
    }

    public String saveMessage(String message) {
        return message;
    }
}