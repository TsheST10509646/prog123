/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.poeloginandres;

/**
 *
 * @author tshep
 */
public class loginClassclass {
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

    



    