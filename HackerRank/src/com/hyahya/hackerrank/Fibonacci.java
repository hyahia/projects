package com.hyahya.hackerrank;

import java.math.BigInteger;
import java.util.Scanner;

public class Fibonacci {

	public static void main(String[] args) {
		long start = System.nanoTime(); 
		System.out.println(fibonacci(6));
		long end1 = System.nanoTime(); 
		System.out.println(fibonacci2(6));
		long end2 = System.nanoTime(); 
		System.out.println((end1-start) + "---" + (end2-end1));
		
		Scanner scanner = new Scanner(System.in);
		BigInteger t1 = BigInteger.valueOf(scanner.nextInt());
		BigInteger t2 = BigInteger.valueOf(scanner.nextInt());
		BigInteger n = BigInteger.valueOf(scanner.nextInt());
        
        System.out.println(fibonacci(n, t1, t2));
        scanner.close();
	}

	public static int fibonacci(int n)  {
	    if(n == 0)
	        return 0;
	    else if(n == 1)
	      return 1;
	   else{
	      return fibonacci(n - 1) + fibonacci(n - 2);
	   }
	}
	
	public static double fibonacci2(int n){
	    double prev=0d, next=1d, result=0d;
	    for (int i = 1; i < n; i++) {
	        result=prev+next;
	        prev=next;
	        next=result;
	    }
	    return result;
	}
	
    public static BigInteger fibonacci(BigInteger n, BigInteger t1, BigInteger t2)  {
	    if(n.intValue() == 1)
	        return t1;
	    else if(n.intValue() == 2)
	      return t2;
	   else{
	      return fibonacci(n.subtract(BigInteger.valueOf(1)), t1, t2).pow(2).add(fibonacci(n.subtract(BigInteger.valueOf(2)), t1, t2));
	   }
	}
}
