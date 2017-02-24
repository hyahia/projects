package test;

import java.io.*; 
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;
public class xx {
public static void main(String[] args) {
    int[] letters = new int[26];
    Scanner input = new Scanner(System.in);
    int N = input.nextInt();
    int[][] map = new int[26][N];
    for(int i=0; i < N; i++){
        String s = input.next();
        for(int j=0; j < s.length(); j++){
            map[s.charAt(j)-97][i] = 1;
        }
    }
    int rows = map.length;
    int cols = map[0].length;
    int count = 26;
    for(int i=0; i< rows; i++ ){

        for(int j=0; j < cols; j++){
            if(map[i][j] != 1){
                count --;
                break;
            }
        }
    }
    System.out.println(count);

}
}