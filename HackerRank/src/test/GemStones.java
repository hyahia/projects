package test;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class GemStones {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int testCases = s.nextInt();
        Set<Character> aSet = null;

        for(int i=0; i<testCases; i++){   
        	String str = s.next();
        	HashSet<Character> temp = new HashSet<Character>();
        	for(char ch : str.toCharArray())
        		temp.add(ch);

        	if(i == 0)
        		aSet = temp;
        	else
        		aSet.retainAll(temp);
        }
        s.close();
        System.out.println(aSet);
    }
}