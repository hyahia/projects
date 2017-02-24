package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

public class SherlockString {

    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        String input = s.next();
        int frequency = 0;
        int totalBadCharacters = 0;
        ArrayList<Integer> frequencies = new ArrayList<Integer>();
        HashSet<Character> chars = new HashSet<Character>();
        
        for(char ch : input.toCharArray()){
        	chars.add(ch);
        }
        
        for(char ch : chars){
            frequency = input.length() - input.replaceAll(""+ch,"").length();
            frequencies.add(frequency);
        }
        
        //frequency = frequencies.get(0);
        HashMap<Integer,Integer> m = new HashMap<>();
        for(int i=0; i<frequencies.size(); i++){
        	if(m.get(frequencies.get(i)) == null){
        		m.put(frequencies.get(i), 1);
        	}else{
        		m.put(frequencies.get(i), m.get(frequencies.get(i))+1);
        	}
        }
        if(m.size() < 2){
            System.out.println("YES");
        }
        else if(m.size() > 2){
            System.out.println("NO");
        }else{
        	Iterator<Integer> it = m.keySet().iterator();
        	if(m.get(it.next()) != 1 && m.get(it.next()) != 1)
                System.out.println("NO");
        	else
                System.out.println("YES");
        }
        s.close();
    }
}