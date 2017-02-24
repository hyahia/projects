package test;

import java.util.Scanner;

public class CaesarCipher {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        in.nextInt();
        String s = in.next();
        int k = in.nextInt();
        
        StringBuilder sb = new StringBuilder();
        for(char ch : s.toCharArray()){
            if(!Character.isAlphabetic(ch))
                sb.append(ch);
            else {
	            char start = Character.isUpperCase(ch)? 'A' : 'a';
	            ch = (char) (start + (ch - start + k)%26 );
	            sb.append(ch);
            }
        }
        in.close();
        System.out.println(sb);
    }
}