package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DistinctEmails {

    public static void main(String[] args) {
        String emailRegex = "([^\\s]+@\\w+.[^\\s]+)";
        Pattern p = Pattern.compile(emailRegex);
        Matcher m = null;
        HashSet<String> result = new HashSet<String>();
       
        Scanner s = new Scanner(System.in);
        int count = s.nextInt();
        String test = s.nextLine();
        
        for(int i=0; i<count; i++){
            test = s.nextLine();
            m = p.matcher(test);
            while(m.find()){
                result.add(m.group());
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