package com.reachout.utils;

import java.security.SecureRandom;

/***
 * Used to generate a random string which is used in the password reset URL
 */
public class CodeGenerator {

    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();
    static final int len = 75;

    //Empty generator
    public CodeGenerator(){
        
    }

    //Generate the random string which is passed into the URL
    public String getUniqueCode(){
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++) 
            sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
        return sb.toString();
    }
    
}