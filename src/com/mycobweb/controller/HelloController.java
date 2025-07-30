// controller/HelloController.java
package com.mycobweb.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.mycobweb.annotation.Injection;
import com.mycobweb.service.HelloService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class HelloController implements HttpHandler {
	@Injection(type = "com.mycobweb.service.HelloService")
	private HelloService helloService;

	@Override
	public void handle(HttpExchange exchange) throws IOException {
	    System.out.println("✅ DEBUG: Request received!"); // Temporary
	    System.err.println("📍 Method: " + exchange.getRequestMethod());
	    System.err.println("📍 URI: " + exchange.getRequestURI());

	    if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
	        sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
	        return;
	    }

	    try {
	        String message = helloService.getWelcomeMessage();
	        String response = String.format("{\"message\": \"%s\"}", message);
	        sendResponse(exchange, 200, response);
	    } catch (Exception e) {
	        e.printStackTrace(); // This is critical
	        sendResponse(exchange, 500, "{\"error\": \"Internal server error\"}");
	    }
	}

	private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
		byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
		exchange.getResponseHeaders().set("Content-Type", "application/json");
		exchange.sendResponseHeaders(statusCode, bytes.length);
		try (OutputStream os = exchange.getResponseBody()) {
			os.write(bytes);
		}
	}
}