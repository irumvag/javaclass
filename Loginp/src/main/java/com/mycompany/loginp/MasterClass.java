/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.loginp;

/**
 *
 * @author Kva
 */
public class MasterClass {
    public static boolean checkEmail(String email){
        return email.contains("@gmail.com")||email.contains("@yahoo.com")||email.contains("@hotmail.com");
    }
    public static int getStringLength(String txt){
        return txt.length();
    }
    public static String changeToUpper(String txt){
        return txt.toUpperCase();
    }
}
