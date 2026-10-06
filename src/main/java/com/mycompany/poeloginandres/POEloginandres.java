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
    
}
}