package com.hyahya.hackerrank;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Abbreviation {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int n = s.nextInt();
        
        for(int i=0; i<n; i++){
            String input = s.nextLine();
            String target = s.nextLine();
            boolean found = false;
			for (int j = 0; j < input.length(); j++) {
				if (Character.isLowerCase(input.charAt(i))) {
					found = found || Pattern.compile(Pattern.quote(input.replaceAll(""+input.charAt(i), "")), Pattern.CASE_INSENSITIVE).matcher(target).find();
				}
			}
            
            System.out.println(found ? "YES" : "NO");
        }
        
        s.close();
    }
}