/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.client.cons.db.repositories;

/**
 *
 * @author kali
 */


import com.mycompany.client.cons.db.entity.User;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    // you can add custom methods later like findByClient(String client)
    // Query SQL pour GET pour avoir tous les clients connectes
    
    @Query(value = "SELECT client FROM users WHERE status = ?1", nativeQuery = true)
    List<String> findUsersByStatus(String status);
    
    // Query SQL pour ISCONNECTED, renvoie le status du client choisi
    @Query(value = "SELECT status FROM users WHERE client = ?1", nativeQuery = true)
    String findStatusOfUser(String user);
    
    
    
    // Insert row for connection 
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users (client, status) VALUES (?1, 'connected')", nativeQuery = true)
    void insertConnectedUser(String user);
    
    // Delete row for this client (disconnected)
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM users WHERE client = ?1", nativeQuery = true)
    void deleteUser(String user);



    
    
}
