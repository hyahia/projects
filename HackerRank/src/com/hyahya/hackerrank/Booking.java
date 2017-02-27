package com.hyahya.hackerrank;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Booking {

	public static void main(String[] args) {
		String positiveRegex = "\\b(nice|good|amazing|wonderful)\\b";
		String negativeRegex = "\\b(bad|sucks|nasty)\\b";

		String[] strs = new String []{"1,very nice bad hotel","2,very good hotel","2,very bad hotel",
									  "3,very nasty hotel","1,very nice hotel","3,very amazing hotel",
									  "4,very wonderful hotel","5,very bad nasty sucks hotel","1,very sucks hotel","3,very nice hotel"};
		
		Pattern positivePattern=Pattern.compile(positiveRegex);
		Pattern negativePattern=Pattern.compile(negativeRegex);
		Matcher positiveMatcher = null;
		Matcher negativeMatcher = null;
		HashMap<String,Integer> result = new HashMap<String, Integer>();
		
		int count = 0;
		String index = null;
		String comment = null;
		for(String str : strs){
			count = 0;
			index = str.split(",")[0];
			comment = str.split(",")[1];
			
			positiveMatcher = positivePattern.matcher(comment);
			negativeMatcher = negativePattern.matcher(comment);

			while(positiveMatcher.find()){
				count++;
			}
			
			while(negativeMatcher.find()){
				count--;
			}
			
			if(result.get(index) != null){
				count += result.get(index);
			}
			result.put(index, count);
		}
		System.out.println(result);
	}
}