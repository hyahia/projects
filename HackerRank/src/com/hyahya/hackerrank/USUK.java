package com.hyahya.hackerrank;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class USUK {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int n = s.nextInt();s.nextLine();
        String[] sentences = new String[n];

        for(int i=0; i<n; i++){
            sentences[i] = s.nextLine();
        }
         
        int t = s.nextInt();s.nextLine();

        int count;
        String word;
        Pattern p;
        Matcher m;
        
        for(int k=0; k<t; k++){
        	count = 0;
            word = s.nextLine();
            
            for(int i=0; i<n; i++){
                
                p = Pattern.compile("\\b"+word+"\\b");
                m = p.matcher(sentences[i]);
                
                while(m.find()){
                    count++;
                }
                
            }
            if(word.contains("our"))
                word = word.replace("our","or");
            else
                word = word.replace("or","our");
               
            for(int i=0; i<n; i++){
                
                p = Pattern.compile("\\b"+word+"\\b");
                m = p.matcher(sentences[i]);
                
                while(m.find()){
                    count++;
                }
                
            }
                
            System.out.println(count);
        }
        
        s.close();
    }
}