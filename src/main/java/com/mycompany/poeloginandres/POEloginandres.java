/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.poeloginandres;

import java.util.Scanner;

/**
 *
 * @author tshep
 */
public class POEloginandres {

    public static void main(String[] args) {
        Scanner myInput = new Scanner(System.in);
// Dectlartions
    String userName;
    String password;
    String cellPhoneNum;

    // USER REGISTRATION

    System.out.println("Enter your username");
    userName = myInput.nextLine();

    while (!loginClassclass.checkUserName(userName)) {
        System.out.println("Username is not correctly formatted");
        System.out.println("Please ensure your username contains an underscore");
        System.out.println("It must be no more than five characters in length");

        System.out.println("Please enter your username");
        userName = myInput.nextLine();
    }

    System.out.println("Username successfully captured");

    // CELLPHONE NUMBER

    System.out.println("Please enter your cell phone number using the South African international formatting starting with +27");
    cellPhoneNum = myInput.nextLine();

    while (!loginClassclass.checkCellPhoneNumber(cellPhoneNum)) {
        System.out.println("Your cell phone number format is incorrect");
        System.out.println("Please enter your cell phone number using the South African international formatting starting with +27");
        cellPhoneNum = myInput.nextLine();
    }

    System.out.println("Cell phone number successfully entered");

    // PASSWORD

    System.out.println("Please enter your password");
    password = myInput.nextLine();

    while (!loginClassclass.checkPasswordComplexity(password)) {
        System.out.println("Your password is incorrect");
        System.out.println("Your password must have at least 8 characters, a capital letter, a number and a special character");

        System.out.println("Please enter your password");
        password = myInput.nextLine();
    }

    System.out.println("Password entered successfully");

    // REGISTRATION

    String registrationMessage =
            loginClassclass.registerUser(userName, password, cellPhoneNum);

    System.out.println(registrationMessage);

   // LOGIN

System.out.println("\n=====LOGIN=====");

boolean loginSuccessful = false;

while (!loginSuccessful) {

    System.out.print("Enter username to login: ");
    String enteredUserName = myInput.nextLine();

    System.out.print("Enter password to login: ");
    String enteredPassword = myInput.nextLine();

    loginSuccessful = loginClassclass.loginUser(enteredUserName, enteredPassword, userName, password);

    System.out.println(loginClassclass.returnLoginStatus(loginSuccessful, userName));
}
  
int choice = 0;

// Ask how many messages the user wants to send
System.out.print("How many messages would you like to send? ");

while (!myInput.hasNextInt()) {
    System.out.println("Please enter a valid number.");
    myInput.nextLine();
    System.out.print("How many messages would you like to send? ");
}

int numberOfMessages = myInput.nextInt();
myInput.nextLine();

// Do not allow a negative number
while (numberOfMessages < 0) {
    System.out.println("Please enter zero or a positive number.");
    System.out.print("How many messages would you like to send? ");

    while (!myInput.hasNextInt()) {
        System.out.println("Please enter a valid number.");
        myInput.nextLine();
        System.out.print("How many messages would you like to send? ");
    }

    numberOfMessages = myInput.nextInt();
    myInput.nextLine();
}

// Count only successfully sent messages
int messagesSent = 0;

while (messagesSent < numberOfMessages) {

    System.out.println("\nMESSAGE "
            + (messagesSent + 1) + " OF " + numberOfMessages);

    System.out.print("Enter recipient cell number (+27...): ");
    String recipient = myInput.nextLine();

    System.out.print("Enter your message: ");
    String messageText = myInput.nextLine();

    message newMessage = new message(recipient, messageText);

    // Validate the message
    if (!newMessage.checkMessageID()) {
        System.out.println("Invalid message ID. Please try again.");
        continue;

    } else if (!newMessage.checkRecipientCell()) {
        System.out.println("Invalid recipient cell number. Please try again.");
        continue;

    } else if (!newMessage.checkMessageLength()) {
        System.out.println("Message exceeds 250 characters. Please try again.");
        continue;
    }

    System.out.println("Message Hash: " + newMessage.createMessageHash());

    int messageChoice = 0;

    // Keep showing the choices until Send or Disregard is selected
    while (messageChoice != 1 && messageChoice != 3) {

        System.out.println("\nChoose what to do with this message:");
        System.out.println("1. Send message");
        System.out.println("2. Store message (Coming soon)");
        System.out.println("3. Disregard message");
        System.out.print("Enter your choice: ");

        while (!myInput.hasNextInt()) {
            System.out.println("Please enter 1, 2, or 3.");
            myInput.nextLine();
            System.out.print("Enter your choice: ");
        }

        messageChoice = myInput.nextInt();
        myInput.nextLine();

        if (messageChoice == 2) {
            System.out.println("Coming soon");
            messageChoice = 0;

        } else if (messageChoice < 1 || messageChoice > 3) {
            System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            messageChoice = 0;
        }
    }

    if (messageChoice == 1) {
        System.out.println(newMessage.sentMessage(1));

        // Count the message only when it is sent
        messagesSent++;

    } else if (messageChoice == 3) {
        System.out.println(newMessage.sentMessage(3));
        System.out.println("Message disregarded. Please enter a replacement.");
        // Do not increase messagesSent.
        // The while loop asks for another message in this same slot.
    }
}

System.out.println("\nYou have sent all "
        + numberOfMessages + " requested messages.");

    }
}  
    

