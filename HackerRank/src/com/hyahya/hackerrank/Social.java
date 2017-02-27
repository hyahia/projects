package com.hyahya.hackerrank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Social {

	public static void main(String[] args) {
		System.out.println(getRankedCourses("hossam"));
	}

	public static List<String> getRankedCourses(String user) {
	    Map<String, Integer> recommendedCourses = new TreeMap<>();
	    ArrayList<String> userCourses = (ArrayList<String>)getAttendedCoursesForUser(user);
	    HashSet<String> userCoursesSet = new HashSet<String>(userCourses);
	    HashSet<String> friends = new HashSet<String>((ArrayList<String>)getDirectFriendsForUser(user));
	    
	    for(String friend : getDirectFriendsForUser(user)){
	    	friends.add(friend);
	    }
	    
		for (String friend : friends) {
			ArrayList<String> friendCourses = (ArrayList<String>) getAttendedCoursesForUser(friend);
			for (String friendCourse : friendCourses) {
				if (!userCoursesSet.contains(friendCourse)) {
					if (recommendedCourses.get(friendCourse) == null) {
						recommendedCourses.put(friendCourse, 1);
					} else {
						recommendedCourses.put(friendCourse, recommendedCourses.get(friendCourse) + 1);
					}
				}
			}
		}
		
	    System.out.println(recommendedCourses);
//	    Map<String, Integer> treeMap = new TreeMap<String, Integer>(recommendedCourses);
	    return Arrays.asList((String[])recommendedCourses.keySet().toArray(new String[recommendedCourses.keySet().size()]));
	}
	
	private static ArrayList<String> getDirectFriendsForUser(String user) {
		System.out.println("Freinds of:" + user);
		HashMap<String, ArrayList<String>> friendsAll = new HashMap<String, ArrayList<String>>();
		friendsAll.put("hossam", new ArrayList<String>(Arrays.asList(new String[] {"magdy","mohammed","adel","hassan"})));
		friendsAll.put("magdy", new ArrayList<String>(Arrays.asList(new String[] {"said","ahmed","mohammed"})));
		friendsAll.put("mohammed", new ArrayList<String>(Arrays.asList(new String[] {"mohsen","ayman"})));
		friendsAll.put("adel", new ArrayList<String>(Arrays.asList(new String[] {"khaled","walid","imam"})));
		friendsAll.put("hassan", new ArrayList<String>(Arrays.asList(new String[] {"ibrahim"})));
		
		return friendsAll.get(user);
	}

	private static ArrayList<String> getAttendedCoursesForUser(String user) {
		System.out.println("Course of:" + user);
		HashMap<String, ArrayList<String>> coursesAll = new HashMap<String, ArrayList<String>>();
		coursesAll.put("hossam", new ArrayList<String>(Arrays.asList(new String[] {"cs 101","cs 102","it 101","it 102"})));
		coursesAll.put("magdy", new ArrayList<String>(Arrays.asList(new String[] {"cs 103","cs 107"})));
		coursesAll.put("mohammed", new ArrayList<String>(Arrays.asList(new String[] {"cs 102","cs 103"})));
		coursesAll.put("adel", new ArrayList<String>(Arrays.asList(new String[] {"it 106","cs 103","cs 107"})));
		coursesAll.put("said", new ArrayList<String>(Arrays.asList(new String[] {"it 108","cs 103",})));
		coursesAll.put("mohsen", new ArrayList<String>(Arrays.asList(new String[] {"it 108","cs 107"})));
		coursesAll.put("ayman", new ArrayList<String>(Arrays.asList(new String[] {"cs 103","it 108"})));
		coursesAll.put("walid", new ArrayList<String>(Arrays.asList(new String[] {"it 108","cs 103",})));
		coursesAll.put("khaled", new ArrayList<String>(Arrays.asList(new String[] {"it 108"})));
		coursesAll.put("imam", new ArrayList<String>(Arrays.asList(new String[] {"it 108","cs 107"})));
		coursesAll.put("ibrahim", new ArrayList<String>(Arrays.asList(new String[] {"cs 103","it 111"})));
		coursesAll.put("ahmed", new ArrayList<String>(Arrays.asList(new String[] {"cs 103","it 112","cs 123","it 101"})));
		coursesAll.put("hassan", new ArrayList<String>(Arrays.asList(new String[] {"cs 113","it 110","cs 109","it 111"})));
		return coursesAll.get(user);
	}
}
