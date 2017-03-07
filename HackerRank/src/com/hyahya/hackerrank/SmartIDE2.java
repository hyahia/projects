package com.hyahya.hackerrank;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmartIDE2 {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        
        while(s.hasNext()){
            sb.append(s.nextLine().trim());
            sb.append("\n");
        }

        Pattern ps = Pattern.compile("((?s)\\/\\*.*?\\*\\/|(?-s)\\/\\/.*|(?-s)\\s*#.*)");
        Matcher ms = ps.matcher(sb.toString());
        
        String result = sb.toString();
        while(ms.find()){
//            System.out.println(ms.group());
            result = result.replace(ms.group(), "");
        }
        
      System.out.println(result);
        
        s.close();
    }
}