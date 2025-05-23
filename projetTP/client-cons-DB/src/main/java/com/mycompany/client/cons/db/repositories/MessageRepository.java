/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.client.cons.db.repositories;

import com.mycompany.client.cons.db.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author kali
 */
public interface MessageRepository extends JpaRepository<Message, Long>{
    
}
