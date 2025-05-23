/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.per.translate.serv;

import static com.mycompany.per.translate.serv.StringHelper.extract_FROM;
import static com.mycompany.per.translate.serv.StringHelper.extract_MSG;
import static com.mycompany.per.translate.serv.StringHelper.extract_TO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author kali
 */


// Lire les messages du topicout 
// Extraire la composante msg 
// envoyer ce message pour traduction au service libre translate
@Service
public class KafkaConsumer {
    
    private final TranslationService translationService;
    
    
    public KafkaConsumer(TranslationService translationService) {
        this.translationService = translationService;
    }
    
     @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
     
    
    @Value("${application.topicin}")
    private String topicIn;
    
    
    
    
    
    @KafkaListener(topics = "${application.topicout}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(String message) {
        
        
        // Extraire la composante du message 
        String msg = extract_MSG(message);
        String dst = extract_TO(message);
        String src = extract_FROM(message);
        
        // Envoyer requete a libretranslator
        String traduction;
        traduction = translationService.translate(msg, "auto", "en");
   
        // Ecrire reponse sur le topicin 
        String reponse = "FROM:"+src+"#TO:"+dst+"#"+traduction;
        kafkaTemplate.send(topicIn, reponse);
         
        
    }


    
}
