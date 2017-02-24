package test;

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class ArrayListSolution {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
    	Scanner in = new Scanner(System.in);
        int count = in.nextInt();
        ArrayList<ArrayList<Integer>> lines = new ArrayList();
        for(int i=0; i<count; i++){
           ArrayList<Integer> line = new ArrayList();
           int itemCount = in.nextInt();
           for(int j=0; j< itemCount; j++){
               line.add(in.nextInt());
           }
           lines.add(line);
        }
        int queryCount = in.nextInt();
        for(int i=0; i<queryCount; i++){
           int x = in.nextInt();
           int y = in.nextInt();
           if(x > lines.size() || y > lines.get(x-1).size()) System.out.println("ERROR!");
           else System.out.println(lines.get(x-1).get(y-1));
        }
    }
}