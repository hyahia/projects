package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DistinctTags {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int count = s.nextInt();s.nextLine();
        Pattern p = Pattern.compile("<([^\\/]\\w*)");
        HashSet<String> result = new HashSet<String>();

        Matcher m = null;
        StringBuilder sb = new StringBuilder();
        
        for(int i=0; i<count; i++){
            sb.append(s.nextLine());
        }
        
        m = p.matcher(sb.toString());
        
        while(m.find()){
            result.add(m.group(1));
        }
        
        List<String> sortedList = new ArrayList<String>(result);
        Collections.sort(sortedList);
        String str = String.join(";", sortedList);

        System.out.println(str);
        
        s.close();
    }
}