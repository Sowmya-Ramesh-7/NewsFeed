package com.newsfeed.util;

public class IdGenerator {
	
	public static String generate(String entityName){
		String id = entityName.toUpperCase() + '_' + generateRandomNumber();
		return id;
	}
	
	private static int generateRandomNumber(){
		return (int) (Math.random() * 10000);
	}
}
