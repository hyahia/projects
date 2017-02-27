package com.hyahya.hackerrank;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class readify {

	public static void main(String[] args) {
		reverseString("we are good we");

	}
	private static String reverseString(String input) {
		String regex = "(\\w+)";
		Pattern p = Pattern.compile(regex, Pattern.MULTILINE + Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(input);

		while (m.find()) {
			input = input.replaceAll(m.group(), reverseWord(m.group()));
		}

		// Prints the modified sentence.
		System.out.println(input);
		return null;
	}
	
	private static String reverseWord(String input) {
		StringBuilder b = new StringBuilder();
		for(int i = input.length()-1; i > -1; i--){
			b.append(input.charAt(i));
		}
		return b.toString();
	}
}
