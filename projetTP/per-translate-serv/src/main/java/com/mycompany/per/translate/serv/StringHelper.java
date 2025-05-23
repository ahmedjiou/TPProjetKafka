/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.per.translate.serv;

/**
 *
 * @author kali
 */
public class StringHelper {
    
    /********************************
     * Extraire le destinataire du
     * message
     * @param message
     * @return 
     ********************************/
    public static String extract_TO(String message) 
    {
         //format du message FROM:ClientX#TO:ClientY#message
        String [] segments=message.split("#");
        return segments[1].substring(3);
    }
    
      /********************************
     * Extraire l'expediteur du
     * message
     * @param message
     * @return 
     ********************************/
    public static String extract_FROM(String message) 
    {
        String [] segments=message.split("#");
        return segments[0].substring(5);
    }
    
      /********************************
     * Extraire le message du
     * message
     * @param message
     * @return 
     ********************************/
    public static String extract_MSG(String message) 
    {
        String [] segments=message.split("#");
        return segments[2];
    }
    
    // Fonction pour extraire la commande du message 
    public static String extraireCommande(String input) {
        if (input == null || !input.contains(":")) {
            return input; // or return null, depending on your use case
        }
        return input.split(":", 2)[0];
    }
}
