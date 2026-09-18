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
     String userName;
     String password;
     String cellPhoneNum;

    private String enteredUserName;
    private String enteredPassword;
    private boolean registered = false;

    // Constructor
    public loginClassclass(String userName, String password, String cellPhoneNum) {
        this.userName = userName;
        this.password = password;
        this.cellPhoneNum = cellPhoneNum;
    }

    // Check username: contains an underscore and is at most 5 characters.
    public boolean checkUserName() {
        return userName != null
                && userName.contains("_")
                && userName.length() <= 5;
    }

    // Check password complexity.
    public boolean checkPasswordComplexity() {
        if (password == null) {
            return false;
        }

        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (int i = 0; i < password.length(); i++) {
            char character = password.charAt(i);

            if (Character.isUpperCase(character)) {
                hasCapital = true;
            }

            if (Character.isDigit(character)) {
                hasNumber = true;
            }

            if (!Character.isLetterOrDigit(character)
                    && !Character.isWhitespace(character)) {
                hasSpecial = true;
            }
        }

        return password.length() >= 8
                && hasCapital
                && hasNumber
                && hasSpecial;
    }

    // Check RSA cellphone number: +27 followed by 9 digits.
    public boolean checkCellPhoneNumber() {
        return cellPhoneNum != null
                && cellPhoneNum.matches("\\+27[0-9]{9}");
    }

    // Validate the details and return the registration message.
    public String registerUser() {
        if (!checkUserName()) {
            return "Username is not correctly formatted. "
                    + "Please ensure your username contains an underscore "
                    + "and is no more than five characters in length.";
        }

        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted. "
                    + "Please ensure your password contains at least eight characters, "
                    + "a capital letter, a number, and a special character.";
        }

        if (!checkCellPhoneNumber()) {
            return "Cellphone number is incorrectly formatted "
                    + "or does not contain an international code. "
                    + "Please use +27 followed by nine digits.";
        }

        registered = true;
        return "User successfully registered.";
    }

    // Store the details entered when attempting to log in.
    public void setLoginDetails(String enteredUserName, String enteredPassword) {
        this.enteredUserName = enteredUserName;
        this.enteredPassword = enteredPassword;
    }

    // Compare the entered details with the registered details.
    public boolean loginUser() {
        return registered
                && userName.equals(enteredUserName)
                && password.equals(enteredPassword);
    }

    // Return a successful or failed login message.
    public String returnLoginStatus() {
        if (loginUser()) {
            return "Welcome " + userName
                    + ", it is great to see you again.";
        }

        return "Username or password incorrect, please try again.";
    }
}

