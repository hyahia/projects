package com.hyahya.hackerrank;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HackerRankLanguages {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int count = scanner.nextInt();scanner.nextLine();
        Pattern langNamePattern = Pattern.compile("\\d+\\s+(\\w*)");
        Matcher langNameMatcher;
        String languages = "C:CPP:JAVA:PYTHON:PERL:PHP:RUBY:CSHARP:HASKELL:CLOJURE:BASH:SCALA:ERLANG:CLISP:LUA:BRAINFUCK:JAVASCRIPT:GO:D:OCAML:R:PASCAL:SBCL:DART: GROOVY:OBJECTIVEC";
        String[] langs = languages.split(":");
        HashSet<String> languageSet = new HashSet<String>();
        languageSet.addAll(Arrays.asList(langs));
        
        String line;
        for(int i=0; i<count; i++){
            line = scanner.nextLine();
            
            langNameMatcher = langNamePattern.matcher(line);
            while(langNameMatcher.find()){
                System.out.println(languageSet.contains(langNameMatcher.group(1)) ? "VALID" : "INVALID");
            }
        }
        
        scanner.close();
    }
}