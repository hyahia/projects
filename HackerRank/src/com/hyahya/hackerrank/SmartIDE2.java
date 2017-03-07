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
            result = result.replace(ms.group(), "");
        }
        
        boolean includeExists = false, semiColonExists = false; 
        
        String[] codeLines = result.split("\n");
        for(String line : codeLines){
        	if(line.replaceAll("\\s", "").startsWith("#include"))
        		includeExists = true;
        	if(line.replaceAll("\\s", "").endsWith(";"))
        		semiColonExists = true;
        }
        System.out.println(result);
        if(!semiColonExists)
          System.out.println("Python");
        else if(includeExists)
            System.out.println("C");
        else
            System.out.println("Java");

        
        s.close();
    }
}