package com.mycobweb.service;

import com.mycobweb.annotation.Injection;
import com.mycobweb.repository.MessageRepository;

public class HelloService {
	 @Injection(type = "com.mycobweb.repository.MessageRepository")
	 private MessageRepository messageRepository;

    public String getWelcomeMessage() {
        
        return messageRepository.getMessage();
    }
}

// service/EchoService.java
