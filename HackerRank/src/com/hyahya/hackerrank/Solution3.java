package com.hyahya.hackerrank;

import java.io.*;
import java.util.*;

public class Solution3 {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner scanner = new Scanner(System.in);
        String s = scanner.nextLine();
        int length = scanner.nextInt();
        
        String min = s.substring(0,length);
        String max = s.substring(0,length);
        String substring = "";
        for(int index = 0; index < s.length()-length; index++){
            System.out.println(index + " - " + length + " - " + (s.length()-length));
            substring = s.substring(index,length);
            if(substring.compareTo(min) > 0){
                min = substring;
            }
            if(substring.compareTo(max) < 0){
                max = substring;
            }
        }
        System.out.println(min);
        System.out.println(max);        
    }
}