package com.hyahya.hackerrank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLAnalyzer {

	public static void main(String[] args) {
        Pattern tagPattern = Pattern.compile("(<\\w[^>]*)");
        Pattern tagNamePattern = Pattern.compile("<(\\w+)");
        Pattern attributePattern = Pattern.compile("(\\w+)\\s*=['\"]");
        
        HashMap<String,HashSet<String>> result = new HashMap<String,HashSet<String>>();
        HashSet<String> attributes = new HashSet<String>();
        
        Matcher tagMatcher, attributeMatcher, tagNameMatcher;
        
        Scanner scanner = new Scanner(System.in);
        int count = scanner.nextInt();scanner.nextLine();
        
        String line;
        for(int i=0; i<count; i++){
            line = scanner.nextLine();
            
            tagMatcher = tagPattern.matcher(line);
            while(tagMatcher.find()){
            	attributes = new HashSet<String>();

                String tagLine = tagMatcher.group(1);
                tagNameMatcher = tagNamePattern.matcher(tagLine);
                tagNameMatcher.find();
                String tagName = tagNameMatcher.group(1);
                
                attributeMatcher = attributePattern.matcher(tagLine);
                while(attributeMatcher.find()){
                    attributes.add(attributeMatcher.group(1));
                }
                
                if(result.get(tagName) != null)
                	attributes.addAll(result.get(tagName));

                result.put(tagName, attributes);
            }
        }

        ArrayList<String> sortedList = new ArrayList<String>(result.keySet());
        Collections.sort(sortedList);
        
        for(String key : sortedList){
        	
        	ArrayList<String> attributesSortedList = new ArrayList<String>(result.get(key));
            Collections.sort(attributesSortedList);
            System.out.println(key + ":" +String.join(",", attributesSortedList));
        }
        
        scanner.close();
    }
}