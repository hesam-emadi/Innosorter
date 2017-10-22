package com.innohub.innosorter.authentication;

import java.util.HashMap;

public class RegistrationServiceDao {

	//stores username and password of the system
	protected HashMap<String, String> systemUsers = new HashMap<String, String>();
	//return messages for creating a new user
	public static String usernameAlreadyExistsMessage = "Username Already Exists";
	public static String successMessage = "New User Successfully Added";
	public static String badPasswordMessage = "Weak Password";

	public String registerUser(String username, String password) {
		int minLength =8;
	    int digit =0;
	    int upCharCount =0;
	    int lowerCharCount =0;
	    
		if(systemUsers.containsKey(username)){
			throw new RuntimeException( usernameAlreadyExistsMessage);
		}
		
		if(password.length() >= minLength){
			
			for(int i = 0; i < password.length(); i++){
				char a = password.charAt(i);
				//if uppercase character found increase count
				if(Character.isUpperCase(a)){
					upCharCount++;
				}
				//if lowercase character found increase count
				if(Character.isLowerCase(a)){
					lowerCharCount++;
				}
				//if digit found increase count
				if(Character.isDigit(a)){
					digit++;
				}
			}
			//if all the conditions are met, add user
			if (digit > 0 && upCharCount >0 && lowerCharCount >0){
				systemUsers.put(username, password);
				return successMessage;
			}
		}
		
		throw new RuntimeException( badPasswordMessage);
	}
	
	public boolean validateUserNamePassword(String username,String password,String role) {
		  throw new RuntimeException("User Name or Password is invaled");
	}

	public String getUserRole() {
		// TODO Auto-generated method stub
		return null;
	}
	
}

	