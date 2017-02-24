package test;

import java.util.Scanner;

public class InsertionSort {
    
    public static void insertIntoSorted(int[] ar) {
        int e = ar[ar.length-1];
        boolean found = false;
        for(int j=ar.length-2; j>=0; j--){
            if(ar[j] < e){
                ar[j+1] = e;
                found = true;
            }else{
                ar[j+1] = ar[j];
            }
            printArray(ar);
            if(found)
            	break;
        }
        if(ar[0] > e){
        	System.out.println(11111);
        	ar[0] = e;
        	printArray(ar);
        }
        
    }
    
    
/* Tail starts here */
     public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int s = in.nextInt();
        int[] ar = new int[s];
         for(int i=0;i<s;i++){
            ar[i]=in.nextInt(); 
         }
         insertIntoSorted(ar);
    }
    
    
    private static void printArray(int[] ar) {
      for(int n: ar){
         System.out.print(n+" ");
      }
        System.out.println("");
   }
    
    
}
