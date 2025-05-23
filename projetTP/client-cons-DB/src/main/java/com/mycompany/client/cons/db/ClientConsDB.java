/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.client.cons.db;

/**
 *
 * @author kali
 */
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ClientConsDB {

    public static void main(String[] args) {
        SpringApplication.run(ClientConsDB.class, args);
    }

    @Bean
    CommandLineRunner runner(KafkaProducer producer) {
        return args -> {
            producer.send("Hello from Java 21 to Kafka topic 'mon-topic'!");
        };
    }
}
