package com.hyahya.hackerrank;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

public class CalendarTest {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String month = in.next();
        String day = in.next();
        String year = in.next();
        Calendar c = Calendar.getInstance();
        c.set(Integer.parseInt(year),Integer.parseInt(month)-1,Integer.parseInt(day));
        System.out.println(c.getTime());
        System.out.println(new SimpleDateFormat("EEEE").format(c.getTime()).toUpperCase());
    }
}
