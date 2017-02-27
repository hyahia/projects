package com.hyahya.hackerrank;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubWordCounter {

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
            System.out.println(word);
            for(int i=0; i<n; i++){
                
                p = Pattern.compile("(\\B"+word+"\\B)");
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