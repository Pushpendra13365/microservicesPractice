package com.auth_service.practice;

public class StarPrint {
    public static void main(String[] args) {

        int arr = 5;
        for (int i = 0; i < arr; i++){
            for (int j = 0; j <arr; j++){
                if (i==2 & j==2){
                    System.out.print(" ");
                } else {
                    System.out.print("*");
                }
            }
            System.out.println();
        }
    }
}
