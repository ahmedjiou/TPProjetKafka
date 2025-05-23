/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.client.cons.db;

/**
 *
 * @author kali
 */
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.mycompany.client.cons.db.StringHelper;
import static com.mycompany.client.cons.db.StringHelper.extract_FROM;
import static com.mycompany.client.cons.db.StringHelper.extract_MSG;
import static com.mycompany.client.cons.db.StringHelper.extract_TO;
import static com.mycompany.client.cons.db.StringHelper.extraireCommande;
import com.mycompany.client.cons.db.entity.Message;
import com.mycompany.client.cons.db.entity.User;
import com.mycompany.client.cons.db.repositories.MessageRepository;
import com.mycompany.client.cons.db.repositories.UserRepository;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.hibernate.internal.CoreLogging.logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

/*

Pour les reponses, basically on peut juste garder la meme structure aue pour un message je suppose 
FROMTOMSG et inserer la reponse a la requete dans MSG et de toute facon ca verifie que c bien destine a moi dans le shell 


*/

/*
#definition du nom des topics pour ces services
application.topicin=topicin
application.topicout=topicout
application.topictechout=topictechout
application.topictechin=topictechin

spring.kafka.consumer.group-id=Client_Messagerie
*/


// A la fin de la conso de chaque topic, on envoie vers le topic in 

@Service
public class KafkaConsumer {
    
     @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
     
    @Value("${application.topictechin}")
    private String topicTechIn;
    
    @Value("${application.topicin}")
    private String topicIn;
    
    @Value("${application.topictechout}")
    private String topicTech;
    
    
    private String MON_NOM;



    
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private MessageRepository messageRepository;
    
    
    

    @KafkaListener(topics = "mon-topic", groupId = "demo-group")
    public void consume(String message) {
        System.out.println("📥 Received: " + message);
    }

    // Lire les messages techniques
    @KafkaListener(topics = "${application.topictechout}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeTechOut(String message) {
        
        String responseTechIn; // String qui contient la rep a push vers le topicTechIn (Pour repondre aux clients)
        /*User user = new User();
        user.setClient(MON_NOM);*/
        
        String source;
        
        MON_NOM = message.split(":")[1];
        // Extraire la commande du message et ses parametres 
        if(extraireCommande(message).equals("GET") ) {
        
            // query la table users pour les users dont le status est connected
            
            List<String> getUsers = userRepository.findUsersByStatus("connected");
            System.out.println("I inserted into the DB" + MON_NOM);
            // Inserer cette reponse dans le message en la formattant
            responseTechIn = "FROM:"+"GET"+"#TO:"+MON_NOM+"#"+getUsers.toString();
            kafkaTemplate.send(topicTechIn, responseTechIn);
            
        } else if (extraireCommande(message).equals("CONNECT") ) {
            
            userRepository.insertConnectedUser(MON_NOM);
            
            
        
        } else if (extraireCommande(message).equals("DISCONNECT") ) {
            // update le status du client a disconnected 
            
            userRepository.deleteUser(MON_NOM);
        
        } else if (extraireCommande(message).equals("DISCONNECTBYEBYE") ) {
            // Pareil que l autre mais on quitte le shell 
            
            userRepository.deleteUser(MON_NOM);
        
        } else if (extraireCommande(message).equals("ISCONNECTED") ) {
        
            // On query la valeur de status pour ce user 
            //String userToCheck = command to extract dest from this particular command
            source = message.split("#")[1];
            //System.out.println(source);
            String statusUser = userRepository.findStatusOfUser(source);
            Pattern pattern = Pattern.compile("ISCONNECTED:(.*?)#");
            Matcher matcher = pattern.matcher(message);

            if (matcher.find()) {
                MON_NOM = matcher.group(1);
                
            } 

            
            // Envoyer reponse vers client sur topictechin
            responseTechIn = "FROM:"+"ISCONNECTED"+"#TO:"+MON_NOM+"#"+source +"is " +statusUser;
            kafkaTemplate.send(topicTechIn, responseTechIn);
            
        } else {
            
            // Renvoyer un message autre pour signaler qu il y a un pb 
        
        }
        
        // Definir des fonctions pour chaque commande extraite 
        
        
        

    }
    
    // Lire les messages pour les archiver 
    @KafkaListener(topics = "${application.topicin}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeOut(String message) {

    
            // Extraire les composantes du message
            String src = extract_FROM(message);
            String dst = extract_TO(message);
            
            String msg_txt = extract_MSG(message);
            // Fonction pour ajouter le message to the DB 
            
            // Pour chaque nouveau message de ce topic, on va l archiver 
            Message msg = new Message();
            msg.setSource(src);
            msg.setDestinataire(dst);
            msg.setMessage(msg_txt);
            
            // On ajoute le message a la table
            messageRepository.save(msg);
            
            
    }


    
}
