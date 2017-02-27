package com.hyahya.hackerrank;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommentsExtractor {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        StringBuilder sb = new StringBuilder();
        
        while(s.hasNext()){
            sb.append(s.nextLine()).append("\r\n");
        }

        Pattern ps = Pattern.compile("((?s)\\/\\*.*?\\*\\/|(?-s)\\/\\/.*)");
        Matcher ms = ps.matcher(sb.toString());
        
        while(ms.find()){
            System.out.println(ms.group());
        }
        
        s.close();
    }
}