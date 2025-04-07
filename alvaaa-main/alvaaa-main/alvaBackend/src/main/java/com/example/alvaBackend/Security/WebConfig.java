package com.example.alvaBackend.Security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Apply CORS to all endpoints




                .allowedOrigins("http://localhost:5173","http://localhost:51155") // Allow requests from localhost:4200



                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Specify allowed methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true) // Allow cookies (if needed)
                .maxAge(3600); // Cache pre-flight requests for 1 hour
    }
}