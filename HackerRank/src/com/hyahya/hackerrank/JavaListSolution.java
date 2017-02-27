package com.hyahya.hackerrank;

import java.util.ArrayList;
import java.util.Scanner;

public class JavaListSolution {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        ArrayList<Integer> numbers = new ArrayList();
        
        for (int i = 0; i < n; i++) {
        	numbers.add(in.nextInt()); 
        }
        int count = in.nextInt();
    	//System.out.println(count);

        for (int i = 0; i < count*2; i++) {
        	String str = in.nextLine();
        	//System.out.println(str);
        	if(str.contains("Insert")){
                //insertsIndecies.add(in.nextInt());
                //insertsValues.add(in.nextInt());
                numbers.add(in.nextInt(),in.nextInt());
//                in.nextLine();
            }else if(str.contains("Delete")){
                //deleteIndecies.add(in.nextInt());
                numbers.remove(in.nextInt());
            }
        }
        for(Integer i : numbers){
            System.out.print(i+" ");
        }
    }
}
