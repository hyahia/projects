package test;

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

class PQStudent{
   private int token;
   private String fname;
   private double cgpa;
   public PQStudent(int id, String fname, double cgpa) {
      super();
      this.token = id;
      this.fname = fname;
      this.cgpa = cgpa;
   }
   public int getToken() {
      return token;
   }
   public String getFname() {
      return fname;
   }
   public double getCgpa() {
      return cgpa;
   }
}
class StudentComparator implements Comparator<PQStudent>
{
    @Override
    public int compare(PQStudent x, PQStudent y)
    {
        if (x.getCgpa() < y.getCgpa())
        {
            return 1;
        }
        if (x.getCgpa() > y.getCgpa())
        {
            return -1;
        }
        else if(x.getFname().equals(y.getFname())){
            return new Double(y.getCgpa()).compareTo(x.getCgpa());
        }
        return x.getFname().compareTo(y.getFname());
    }
}

public class PQueueSolution {

    public static void main(String[] args) {
      Scanner in = new Scanner(System.in);
      int totalEvents = Integer.parseInt(in.nextLine());
      Comparator<PQStudent> comparator = new StudentComparator();
      PriorityQueue<PQStudent> q = new PriorityQueue<PQStudent>(10, comparator);
      
      if(totalEvents == 0){
          System.out.println("EMPTY");
          return;
      }
      while(totalEvents>0){
         String event = in.next();
         if("ENTER".equals(event)){
             String fName = in.next();
             double cgpa = in.nextDouble();
             int token = in.nextInt();
             q.add(new PQStudent(token,fName,cgpa));
         }else{
             //event = in.next();
             if(!q.isEmpty()) q.poll();
//             else{
//                 System.out.println("EMPTY");
//             }
         }            
         totalEvents--;
      }
      if(q.isEmpty()) System.out.println("EMPTY");

        for(int i=q.size(); i>0; i--){
            if(q.isEmpty()) System.out.println("EMPTY");
             else{
                 System.out.println(q.poll().getFname());
             }
        }
    }
}
