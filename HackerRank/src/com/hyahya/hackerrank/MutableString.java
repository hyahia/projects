package com.hyahya.hackerrank;

public class MutableString {

	public static void main(String[] args) {
		String a = "11111111111";
		a.replaceAll("1", "2");
		System.out.println(a);
	}

	private static void mutateString(String x){
		x = x.replaceAll("1", "2");
		System.out.println(x);
	}
}
