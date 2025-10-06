package com.auth_service.practice;

public class ReverseStringJava8 {
    public static void main(String [] args){
        String str = "Maharaj";
        String reversedString = new StringBuilder(str).reverse().toString();
        System.out.println(reversedString);
    }
}
