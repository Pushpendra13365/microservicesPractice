package com.auth_service.practice;

import java.util.Arrays;

public class DoubleEqualsJava {
    public static void main(String[] args) {
        boolean result = primitive();
        //System.out.println(result);
        Object object = objectType();
        if (object instanceof String[]){
            String[] s = (String[]) object;
            System.out.println("Object comparing"+ Arrays.toString(s));
        }
    }

    public static boolean primitive() {
        // == using with primitive datatype
        int a = 5;
        int b = 5;
        return (a == b);
    }

    public static Object objectType(){
        String str = "Hello";
        String str1 = "Hello";
        return new String[]{str,str1};
    }
}
