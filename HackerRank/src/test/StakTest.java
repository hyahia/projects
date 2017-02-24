package test;

import java.util.*;
class StakTest{
   
   public static void main(String []argh)
   {
      Scanner sc = new Scanner(System.in);
      ArrayList<String> input = new ArrayList();
      Stack<String> open = new Stack();
      Stack<String> close = new Stack();
      //while (sc.hasNext()) {
      for(int i = 0; i < 18; i++){
         String line=sc.next();
         //Complete the code
         input.add(line); 
      }
       for(String line : input){
    	   open = new Stack();
    	   close = new Stack();
           if(line.length() % 2 != 0){
               System.out.println(false);
               continue;
           }else if(line.startsWith(")")||line.startsWith("}")||line.startsWith("]")){
               System.out.println(false);
               continue;
           }else{
               boolean balanced = true;
               for(int i=0; i<line.length(); i++){
                   if("{".equals(line.substring(i,i+1)) || "[".equals(line.substring(i,i+1)) || "(".equals(line.substring(i,i+1))){
                	   open.push(line.substring(i,i+1));
                   }
                   if("}".equals(line.substring(i,i+1)) || "]".equals(line.substring(i,i+1)) || ")".equals(line.substring(i,i+1))){
                	   close.push(line.substring(i,i+1));
                   }
               }
               
               if(open.size() != close.size()){
                   System.out.println(false);
                   continue;
               }
               
               for(int i=0; i < open.size(); i++){
            	   String openStr = open.pop();
            	   String closeStr = close.pop();
            	   if(openStr.equals('{') && !closeStr.equals('}')){
                       balanced = false;
                       continue;
                   }
            	   if(openStr.equals('[') && !closeStr.equals(']')){
                       balanced = false;
                       continue;
                   }
            	   if(openStr.equals('(') && !closeStr.equals(')')){
                       balanced = false;
                       continue;
                   }
               }
               System.out.println(balanced);
           }
       }
      
   }
}
