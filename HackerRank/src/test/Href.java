package test;

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Href {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int count = s.nextInt();s.nextLine();
        Pattern p = Pattern.compile("<a\\s+href\\s*=\\s*\"([^\"]*)\"[^>]*>((?:<.?>)*([^<]*))");
        
        Matcher m = null;
        StringBuilder sb = new StringBuilder();
        
        for(int i=0; i<count; i++){
            sb.append(s.nextLine());
        }
        
        m = p.matcher(sb.toString());
        
        while(m.find()){
            System.out.println(m.group(1)+ "," + m.group(3));
        }
        
        s.close();
    }
}