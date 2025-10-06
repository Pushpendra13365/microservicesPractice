package com.auth_service.practice;

public class ReverseStringLoop {
    public static void main(String[] args) {
        String str = "Mahawati";
        for(int i = str.length()-1; i >=0;i--){
            char c = str.charAt(i);
            System.out.print(c);
        }
    }
}
