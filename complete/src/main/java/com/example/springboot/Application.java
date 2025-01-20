package com.example.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * The main entry point for the Spring Boot application.
 * This class is responsible for bootstrapping and launching the application.
 */
@SpringBootApplication // Automatically includes component scanning
@ComponentScan(basePackages = "com.example.springboot") // Ensure that it scans the package for components like Repository and Controller
public class Application {

    /**
     * The main method that serves as the entry point for the application.
     **/
    public static void main(String[] args) {
        // Launches the Spring Boot application by creating the application context,
        // initializing Spring Beans, and starting the embedded server.
        SpringApplication.run(Application.class, args);
    }
}
