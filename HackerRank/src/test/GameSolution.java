package test;

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class GameSolution {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner in = new Scanner(System.in);
        int count = in.nextInt();
        Integer[] hops = new Integer[count];
        ArrayList<Integer[]> games = new ArrayList();
        for(int i=0; i<count; i++){
            int length = in.nextInt();
            int m = in.nextInt();
            Integer[] game = new Integer[length];
            for(int j=0; j<length; j++){
                game[j] = in.nextInt();
            }
            games.add(game);
            hops[i] = m;
        }
        //System.out.println(games);
        int index = 0;
        for(Integer[] game : games){
            if(game[0] == 1){
                System.out.println("NO");
                break;
            }
            
            boolean won = false;
            for(int i=0; i<game.length;){
                int element = game[i];
                //System.out.println(element+","+index+","+game.length+","+(index+hops[index]));
                if(i+1 >= game.length || i+hops[index] >= game.length){
                    System.out.println("YES");
                    won = true;
                    break;
                }
                if(game[i+hops[index]] == 0 || game[i+1] == 0){
                    i = Math.max(i+hops[index],i+1);
                    continue;
                }
//                if(game[i+1] == 0){
//                    i++;
//                    continue;
//                }
                if(i>0 && game[i-1] == 0 && game[i+hops[index]-1] == 0 && i>1){
                    i--;
                    continue;
                }
                else{
                    break;
                }
                
            }
            if(!won)System.out.println("NO");
            index++;
        }
    }
}
    