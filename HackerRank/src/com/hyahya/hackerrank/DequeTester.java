package com.hyahya.hackerrank;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class DequeTester {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        Deque<Integer> deque = new ArrayDeque<>();
        Set<Integer> unique = new HashSet<>();
        int n = in.nextInt();
        int m = in.nextInt();
        int max = 0;
        
        for (int i = 0; i < n; i++) {
            int num = in.nextInt();
            deque.add(num);
            unique.add(num);
            if(deque.size() == m){
                int first = (int) deque.pop();
                if(unique.size() > max){
                    max = unique.size();
                }
                //deque.removeFirst();
                if(!deque.contains(first)){
                //    unique.remove(first);
                } 
            }
        }
        
        System.out.println(max);
    }
}
