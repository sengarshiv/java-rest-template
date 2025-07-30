// util/LoggerUtil.java
package com.mycobweb.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import com.mycobweb.config.AppConfig;

public class LoggerUtil {

	public static Logger createLogger(String name) {
		Logger logger = Logger.getLogger(name);
		logger.setUseParentHandlers(false);

		// Remove existing handlers (avoid duplicates)
		for (Handler handler : logger.getHandlers()) {
			logger.removeHandler(handler);
		}

		// ✅ Step 1: Create logs directory if it doesn't exist
		Path logDir = Paths.get(AppConfig.LOG_FILE_NAME()).getParent();
		if (logDir != null) {
			try {
				Files.createDirectories(logDir); // Creates `logs/` folder
			} catch (IOException e) {
				System.err.println("❌ Failed to create log directory: " + logDir);
				e.printStackTrace();
				// Proceed anyway — maybe disk is read-only, etc.
			}
		}

		// ✅ Step 2: Now create FileHandler
		try {
			FileHandler fileHandler = new FileHandler(AppConfig.LOG_FILE_NAME(), true); // append = true
			fileHandler.setLevel(Level.ALL);
			fileHandler.setFormatter(new SimpleFormatter());
			logger.addHandler(fileHandler);
		} catch (IOException e) {
			System.err.println("❌ Failed to create log file: " + AppConfig.LOG_FILE_NAME());
			e.printStackTrace();
		}

		// Optional: Add console handler
		if (AppConfig.LOG_TO_CONSOLE()) {
			ConsoleHandler consoleHandler = new ConsoleHandler();
			consoleHandler.setLevel(Level.INFO);
			consoleHandler.setFormatter(new SimpleFormatter());
			logger.addHandler(consoleHandler);
		}

		logger.setLevel(Level.ALL);
		return logger;
	}
}