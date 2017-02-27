package com.hyahya.hackerrank;

import java.math.BigDecimal;
import java.util.*;
class Solution4{

    public static void main(String []args){
        //Input
        Scanner sc= new Scanner(System.in);
        int n=sc.nextInt();
        String []s=new String[n+2];
        String []s2=new String[n];
        for(int i=0;i<n;i++){
            s[i]=sc.next();
        }
        for(int i=0;i<n;i++){
            s2[i]=s[i];
        }
      	sc.close();
      	for(int i = 0; i < s.length-2; i++){
            for(int j = 0; j < s.length-2; j++){
                if(new BigDecimal(s[i]).compareTo(new BigDecimal(s[j])) == 0)
                    continue;
                if(new BigDecimal(s[i]).compareTo(new BigDecimal(s[j])) >= 0){
                    String temp = s[j];
                    s[j] = s[i];
                    s[i] = temp;
                }
            }
        }
        //Output
        for(int i=0;i<n;i++)
        {
            System.out.println(s[i]);
        }
    }

}