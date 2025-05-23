/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.per.translate.serv;

/**
 *
 * @author kali
 */
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class TranslationService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String translate(String text, String source, String target) {
        // Prepare the body to send to LibreTranslate API
        Map<String, String> body = new HashMap<>();
        body.put("q", text);
        body.put("source", source);
        body.put("target", target);
        body.put("format", "text");

        // Set headers to JSON
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        // Send POST request
        ResponseEntity<Map> response = restTemplate.postForEntity(
                "http://localhost:5000/translate",
                request,
                Map.class
        );

        // Get the translated text
        Map responseBody = response.getBody();
        if (responseBody != null && responseBody.containsKey("translatedText")) {
            return (String) responseBody.get("translatedText");
        }

        return "Translation failed.";
    }
}