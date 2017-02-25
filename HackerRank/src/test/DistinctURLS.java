package test;

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class DistinctURLS {

    public static void main(String[] args) {
        String urlRegex = "(http:\\/\\/|https:\\/\\/)(www\\.|)([-a-zA-Z0-9.]+\\.[-a-zA-Z0-9]+)";
        Pattern p = Pattern.compile(urlRegex);
        Matcher m = null;
        HashSet<String> result = new HashSet<String>();
       
        Scanner s = new Scanner(System.in);
        int count = s.nextInt();
        String test = s.nextLine();
        
        for(int i=0; i<count; i++){
            test = s.nextLine();
            m = p.matcher(test);
            while(m.find()){
                result.add(m.group(3));
            }
        }
        
        StringBuilder sb = new StringBuilder();
        List<String> sortedList = new ArrayList<String>(result);
        Collections.sort(sortedList);
        
        for(String str : sortedList){
            if(str.endsWith(".")) str = str.substring(0,str.length()-1);
            sb.append(str).append(";");
        }
        
        System.out.println(sb.toString().substring(0,sb.length()-1));
        
        s.close();
    
    }
}