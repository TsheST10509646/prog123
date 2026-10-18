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

        while (choice != 3) {

            System.out.println("\n===== MESSAGING MENU =====");
            System.out.println("1. Send Message");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Quit");
            System.out.print("Choose an option: ");

            if (!myInput.hasNextInt()) {
                System.out.println(
                        "Please enter a number from 1 to 3."
                );
                myInput.nextLine();
                continue;
            }

            choice = myInput.nextInt();
            myInput.nextLine();

            switch (choice) {

                case 1:
                    System.out.print(
                            "Enter recipient cell number (+27...): "
                    );
                    String recipient = myInput.nextLine();

                    System.out.print("Enter your message: ");
                    String messageText = myInput.nextLine();

                    message newMessage =
                            new message(recipient, messageText);

                    if (!newMessage.checkMessageID()) {
                        System.out.println(
                                "Invalid message ID."
                        );
                    } else if (!newMessage.checkRecipientCell()) {
                        System.out.println(
                                "Invalid recipient cell number."
                        );
                    } else if (!newMessage.checkMessageLength()) {
                        System.out.println(
                                "Message exceeds 250 characters."
                        );
                    } else {
                        System.out.println(
                                "Message Hash: "
                                + newMessage.createMessageHash()
                        );

                        System.out.println(
                                "1. Send message"
                        );
                        System.out.println(
                                "2. Store message"
                        );
                        System.out.println(
                                "3. Disregard message"
                        );
                        System.out.print("Choose an option: ");

                        if (myInput.hasNextInt()) {
                            int messageChoice = myInput.nextInt();
                            myInput.nextLine();

                            System.out.println(
                                    newMessage.sentMessage(
                                            messageChoice
                                    )
                            );
                        } else {
                            System.out.println(
                                    "Invalid option."
                            );
                            myInput.nextLine();
                        }
                    }
                    break;

                case 2:
                    System.out.println(
                            "Recently sent messages:"
                    );

                    message messageManager = new message(
                            "", ""
                    );

                    System.out.println(
                            messageManager.printMessages()
                    );
                    break;

                case 3:
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println(
                            "Invalid option. Please try again."
                    );
                    break;
            }
        }

        System.out.println(
                "Total messages sent: "
                + message.returnTotalMessages()
        );

        myInput.close();
    }
}  
    

