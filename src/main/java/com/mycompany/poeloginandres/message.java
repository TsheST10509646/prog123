/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poeloginandres;

import java.util.Random;

/**
 *
 * @author tshep
 */
public class message {
      private String messageID;
    private String recipient;
    private String Message;
    private String MessageHash;
    
    private static int totalMessages =0;
    
    // constructor
    public message(String recipient,String Message){
    this.recipient= recipient;
    this.Message= Message;
    
    Random myRandom = new Random();
    //generate 10 digit messageID
    long id=1000000000L+ myRandom.nextInt(900000000);
    messageID=String.valueOf(id);
    }
    public boolean checkMessageID(){
    return messageID.length()<=10;
    }
    public boolean checkRecipientCell(){
    return recipient.matches("\\+27[0-9]{9}");
    }
    public boolean checkMessageLength(){
    return Message !=null && Message.length()<=250;
    }
    public String createMessageHash(){
    String[] words=Message.trim().split("\\s+");
    
    String firstTwoDigits= messageID.substring(0, 2);
    String firstWord = words[0];
    String lastWord = words [words.length -1];
    
    MessageHash=firstTwoDigits+":"+totalMessages+ ":"+firstWord.toUpperCase()
            +lastWord.toUpperCase();
    return MessageHash;
    
    }
     public String getMessageID(){
     return messageID;
     }
     public String getRecipient(){
     return recipient;
     }
     public String getMessage(){
     return Message;
     }
     public String getMessageHash(){
     return MessageHash;
     }
     public static int returnTotalMessages(){
      return totalMessages;
     }
     public static void incrementTotalMessages(){
         totalMessages++;
     }
}


