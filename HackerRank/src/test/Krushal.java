package test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Krushal {

    public static void main(String[] args) {
    	Scanner s = new Scanner(System.in);
    	int n = s.nextInt();
    	int m = s.nextInt();
    	ArrayList<Edge> edges = new ArrayList<>();
    	ArrayList<Edge> targetEdges = new ArrayList<>();
    	int sum =0;
    	
    	for(int i=0; i<m; i++){
    		edges.add(new Edge(s.nextInt(), s.nextInt(), s.nextInt()));
    	}
    	
    	Collections.sort(edges);
    	
    	for(Edge e : edges){
    		if(targetEdges.size() == n-1)
    			break;
    			
    		if(targetEdges.isEmpty()){
    			targetEdges.add(e);
    			sum += e.getWeight();
    			continue;
    		}
    		
    		if(!isCircle(e,targetEdges)){
    			targetEdges.add(e);
    			sum += e.getWeight();
    		}
    	}
    	
    	System.out.println(sum);
//    	System.out.println(edges);
//    	System.out.println(targetEdges);
    	s.close();
    }
    
    private static boolean isCircle(Edge e, ArrayList<Edge> edges){
    	if(edges.contains(e)){
    		return true;
    	}
    	for(Edge e1 : edges){
			if(e.getStart() == e1.getStart()){
				if(edges.contains(new Edge(e.getFinish(),e1.getFinish(),-1)) || edges.contains(new Edge(e1.getFinish(),e.getFinish(),-1)))
					return true;
			}
			if(e.getStart() == e1.getFinish()){
				if(edges.contains(new Edge(e.getFinish(),e1.getStart(),-1)) || edges.contains(new Edge(e1.getFinish(),e.getStart(),-1)))
					return true;
			}
			if(e.getFinish() == e1.getStart()){
				if(edges.contains(new Edge(e.getStart(),e1.getStart(),-1)) || edges.contains(new Edge(e1.getStart(),e.getStart(),-1)))
					return true;
			}
			if(e.getFinish() == e1.getFinish()){
				if(edges.contains(new Edge(e.getStart(),e1.getFinish(),-1)) || edges.contains(new Edge(e1.getStart(),e.getFinish(),-1)))
					return true;
			}
		}
    	
    	return false;
    }
}
class Edge implements Comparable<Edge>{

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + finish;
		result = prime * result + start;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		if (finish != other.finish)
			return false;
		if (start != other.start)
			return false;
		return true;
	}

	private int start;
    private int finish;
    private int weight;
    
	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getFinish() {
		return finish;
	}

	public void setFinish(int finish) {
		this.finish = finish;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

    public Edge(int start, int finish, int weight){
        this.start = start;
        this.finish = finish;
        this.weight = weight;
    }

	@Override
	public int compareTo(Edge o) {
		return this.weight - o.getWeight();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Edge [start=").append(start).append(", finish=").append(finish).append(", weight=")
				.append(weight).append("]");
		return builder.toString();
	}
}