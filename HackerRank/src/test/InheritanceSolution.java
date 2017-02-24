package test;
import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;
import java.lang.reflect.*;
import java.security.Permission;

public class InheritanceSolution {
		 public static void main(String[] args) {
		   DoNotTerminate.forbidExit();
		   try {
		    Calculate cal = new Calculate();
		    int T = cal.get_int_val();
		    while (T--> 0) {
		     double volume = 0.0;
		     int ch = cal.get_int_val();
		     if (ch == 1) {
		      int a = cal.get_int_val();
		      volume = Calculate.do_calc().get_volume(a);
		     } else if (ch == 2) {
		      int l = cal.get_int_val();
		      int b = cal.get_int_val();
		      int h = cal.get_int_val();
		      volume = Calculate.do_calc().get_volume(l, b, h);

		     } else if (ch == 3) {
		      double r = cal.get_double_val();
		      volume = Calculate.do_calc().get_volume(r);

		     } else if (ch == 4) {
		      double r = cal.get_double_val();
		      double h = cal.get_double_val();
		      volume = Calculate.do_calc().get_volume(r, h);

		     }
		     cal.output.display(volume);
		    }

		   } catch (NumberFormatException e) {
		    System.out.print(e);
		   } catch (IOException e) {
		    e.printStackTrace();
		   } catch (DoNotTerminate.ExitTrappedException e) {
		    System.out.println("Unsuccessful Termination!!");
		   }


		  } //end of main
		} //end of Solution

		/**
		 *This class prevents the user form using System.exit(0)
		 * from terminating the program abnormally.
		 *
		 */
		class DoNotTerminate {

		 public static class ExitTrappedException extends SecurityException {}

		 public static void forbidExit() {
		  final SecurityManager securityManager = new SecurityManager() {
		   @Override
		   public void checkPermission(Permission permission) {
		    if (permission.getName().contains("exitVM")) {
		     throw new ExitTrappedException();
		    }
		   }
		  };
		  System.setSecurityManager(securityManager);
		 }
		} //end of Do_Not_Terminate

class Calculate{
    static Scanner in = new Scanner(System.in);
    static DecimalFormat df = new DecimalFormat("0.000");
    static Volume volume = new Volume();
    static Output output = new Output();
    
    public int get_int_val() throws IOException{
        return in.nextInt();
    }
    public double get_double_val() throws IOException{
        return in.nextDouble();
    }
    static class Volume{
        public static double get_volume(int a){
            if(a < 0) throw new java.lang.NumberFormatException("All the values must be positive");
            return a*a*a;
        }
        public static double get_volume(int l, int b, int h){
            if(l<0 || b<0 || h<0) throw new java.lang.NumberFormatException("All the values must be positive");
            return l*b*h;
        }
        public static double get_volume(double r){
            if(r < 0) throw new java.lang.NumberFormatException("All the values must be positive");
            return 2*r*r*r*3.14159265/3;
        }
        public static double get_volume(double r, double h){
            if(r<0 || h<0) throw new java.lang.NumberFormatException("All the values must be positive");
            return r*r*3.14159265*h;
        }
    }
    public static Volume do_calc(){
        return volume;
    } 
    static class Output{
        public static void display(double d){
            System.out.println(df.format(d));
        }
    }
}
