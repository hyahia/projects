package com.hyahya.hackerrank;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Scanner;

//public class CheckerTest {
//
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//	}
//
//}

class Player{
    String name;
    int score;
    
    Player(String name, int score){
        this.name = name;
        this.score = score;
    }
}
class Checker implements Comparator<Player>{
    public int compare(Player p, Player other){
        if(p.score > other.score){
            return 1;
        }else if(p.score < other.score){
            return -1;
        }else{
            return p.name.compareTo(other.name);
        }
    }
}
class CheckerTest {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();

        Player player[] = new Player[n];
        Checker checker = new Checker();
        
        for(int i=0; i < n; i++){
            player[i] = new Player(scan.next(), scan.nextInt());
        }
        scan.close();
     
        Arrays.sort(player, checker);
        for(int i = 0; i < player.length; i++){
            System.out.printf("%s %s\n", player[i].name, player[i].score);
        }
    }
}