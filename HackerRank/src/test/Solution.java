package test;

import java.util.*;

class Student{
   private int id;
   private String fname;
   private double cgpa;
   public Student(int id, String fname, double cgpa) {
      super();
      this.id = id;
      this.fname = fname;
      this.cgpa = cgpa;
   }
   public int getId() {
      return id;
   }
   public String getFname() {
      return fname;
   }
   public double getCgpa() {
      return cgpa;
   }
}
class Checker1 implements Comparator<Student>{
    public int compare(Student p, Student other){
        if(p.getCgpa() > other.getCgpa()){
            return 1;
        }else if(p.getCgpa() < other.getCgpa()){
            return -1;
        }if(p.getFname().compareTo(other.getFname()) > 0){
            return 1;
        }else if(p.getFname().compareTo(other.getFname()) < 0){
            return -1;
        }else{
            return p.getId() - other.getId();
        }
    }
}
//Complete the code
public class Solution
{
   public static void main(String[] args){
      Scanner in = new Scanner(System.in);
      int testCases = Integer.parseInt(in.nextLine());
      
      List<Student> studentList = new ArrayList<Student>();
      while(testCases>0){
         int id = in.nextInt();
         String fname = in.next();
         double cgpa = in.nextDouble();
         
         Student st = new Student(id, fname, cgpa);
         studentList.add(st);
         
         testCases--;
      }
               Checker1 checker = new Checker1();

              Collections.sort(studentList, checker);

         for(Student st: studentList){
         System.out.println(st.getFname());
      }
   }
}
