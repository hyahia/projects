package test;

import java.util.Scanner;

public class StringContigousSolution {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int t = s.nextInt();
        String text = s.nextLine();
        int minDeletions = 0;
        for(int i=0; i<t; i++){
            text = s.nextLine(); 
            minDeletions = 0;
            for(int j=0; j<text.length()-1; j++){
                if(text.charAt(j) == text.charAt(j+1)){
                    minDeletions++;
                }
            }
            
            System.out.println(minDeletions);
        }
        
        s.close();
    }
}