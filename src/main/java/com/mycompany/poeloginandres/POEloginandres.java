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

    String userName;
    String password;
    String cellPhoneNum;

    // USER REGISTRATION

    System.out.println("Enter your username");
    userName = myInput.nextLine();

    while (!checkUserName(userName)) {
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

    while (!checkCellPhoneNumber(cellPhoneNum)) {
        System.out.println("Your cell phone number format is incorrect");
        System.out.println("Please enter your cell phone number using the South African international formatting starting with +27");
        cellPhoneNum = myInput.nextLine();
    }

    System.out.println("Cell phone number successfully entered");

    // PASSWORD

    System.out.println("Please enter your password");
    password = myInput.nextLine();

    while (!checkPasswordComplexity(password)) {
        System.out.println("Your password is incorrect");
        System.out.println("Your password must have at least 8 characters, a capital letter, a number and a special character");

        System.out.println("Please enter your password");
        password = myInput.nextLine();
    }

    System.out.println("Password entered successfully");

    // REGISTRATION

    String registrationMessage =
            registerUser(userName, password, cellPhoneNum);

    System.out.println(registrationMessage);

    // LOGIN

    System.out.println("\n=====LOGIN=====");

    System.out.print("Enter username to login: ");
    String enteredUserName = myInput.nextLine();

    System.out.print("Enter password to login: ");
    String enteredPassword = myInput.nextLine();

    boolean loginSuccessful =
            loginUser(enteredUserName, enteredPassword, userName, password);

    System.out.println(
            returnLoginStatus(loginSuccessful, userName)
    );
}
// method to check username
public static boolean checkUserName(String userName)
{
boolean hasUnderScore;
boolean correctLenght;

//string manipulation
hasUnderScore= userName.indexOf('_')>=0;
correctLenght= userName.length()<=5;
if (hasUnderScore&&correctLenght){
return true;
}
else{
return false;}

}
// method to check password
public static boolean checkPasswordComplexity(String password){

    boolean hasCapital=false;
    boolean hasNumber=false;
    boolean hasSpecial=false;
    
    for(int i=0;i<password.length();i++){
    
        char character=password.charAt(i);
        
        if(Character.isUpperCase(character)){
        hasCapital=true;
        }
         if(Character.isDigit(character)){
        hasNumber=true;
         }
         if(Character.isLetterOrDigit(character)){
        hasSpecial=true;
         }
    }
    return password.length()>=8
      &&hasCapital
      &&hasNumber
      &&hasSpecial;
}
//Method to check cell number for RSA
public static boolean checkCellPhoneNumber(String cellPhoneNum){

    return cellPhoneNum.matches("\\+27[0-9]{9}");
}
//registration method
public static String registerUser(String userName,String password,String cellPhoneNum){
if(!checkUserName(userName)){
return  "Username is not correctly formatted"
        + "please ensure your username contain an underscore"
        + "Is no more than five characters in length";}
   
if(!checkPasswordComplexity(password)){
return  "Password is not correctly formatted"
        + "please ensure your username contain at least eight characters "
        + "a capital letter,a number, and a special character.";}

if(!checkCellPhoneNumber(cellPhoneNum)){
return  "Cellphone number is incorrectly formatted or does not contain an international code";}

return"Username password and cell phone number successfully captuure";
}
//login method
public static boolean loginUser(String enteredUserName,String enteredPassword,String userName,String password){

return enteredUserName.equals(userName)&&enteredPassword.equals(password);
}
//Return login status
public static String returnLoginStatus(boolean loginSuccessful,String userName){

    if(loginSuccessful){
    return"welcome"+userName+ ",it is great to see you again.";}
    else{
    return "Username or password incorrect,please try again.";}
            
}
}

    

