package com.mycobweb;

import com.mycobweb.config.AppConfig;
import com.mycobweb.controller.HelloController;
import com.mycobweb.di.Injector;
import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class SimpleRestServer {
    public static void main(String[] args) {
        try {
            var port = AppConfig.SERVER_PORT();
            var injector = new Injector();  // ← Your custom DI container

            // ✅ Let Injector create the controller (and inject dependencies)
            HelloController controller = injector.getInstance(HelloController.class);

            // ✅ Now register the already-injected instance
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/hello", controller);
            server.setExecutor(null);
            server.start();

            System.out.println("✅ Server started on http://localhost:" + port + "/hello");

            // 👇 Prevent main thread from exiting
            Thread.currentThread().join();

        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}