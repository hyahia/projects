package test;

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class MaxXOR {
/*
 * Complete the function below.
 */

    static int maxXor(int l, int r) {
        int max = 0;
        int start = Math.min(l,r);
        int end = Math.max(l,r);
        System.out.println(""+start+"*"+end);

        for(int i=start; i<=end; i++){
            for(int j=start; j<=i; j++){
                System.out.println(""+j+"*"+i+"="+(i^j));
                max = Math.max(max,i^j);
            }
        }
        return max;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int res;
        int _l;
        _l = Integer.parseInt(in.nextLine());
        
        int _r;
        _r = Integer.parseInt(in.nextLine());
        
        res = maxXor(_l, _r);
        System.out.println(res);
        
    }
}
