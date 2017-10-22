package com.innohub.innosorter.authentication;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.innohub.innosorter.util.ApplicationConstants;

public class AuthenticationServiceTest {

	AuthenticationService authenticationService;
	@Before
	public void setUp(){
		authenticationService = new AuthenticationService();
	}
	
	@Rule 
	public ExpectedException expected = ExpectedException.none();
	
	@Test 
	public void shouldThrowExceptionWhenPasswordIsNull(){
		
		expected.expect(RuntimeException.class);
		expected.expectMessage(ApplicationConstants.NULL_PASSWORD_MSG);
		
		authenticationService.addNewUser("newUsername", null);
	}
	
	@Test 
	public void shouldThrowExceptionWhenPasswordIsEmpty(){
		
		expected.expect(RuntimeException.class);
		expected.expectMessage(ApplicationConstants.EMPTY_PASSWORD_MSG);
		
		authenticationService.addNewUser("newUsername", "");
	}

	@Test
	public void shouldNotAllowUserToHaveDuplicateUserName(){
		
		authenticationService.addNewUser("Aman", "A123456fd");
		
		expected.expect(RuntimeException.class);
		expected.expectMessage(ApplicationConstants.USERNAME_ALREADY_EXISTS_MSG);
		
		authenticationService.addNewUser("Aman", "B1234trw");;
	}
	
	@Test
	public void shouldAllowUserToSetPassword(){
		
		//correct password
		String result = authenticationService.addNewUser("NotAman", "Abc12345");
		assertEquals(ApplicationConstants.SUCCESSFULLY_ADDED_USER_MSG, result);
	}
	
	@Test
	public void shouldThrowExceptionOfNewPasswordBeDifferentFromOldPasswordWhenUpdatingPassword(){
		
		authenticationService.addNewUser("NotAman", "Abc12345");
		
		expected.expect(RuntimeException.class);
		expected.expectMessage(ApplicationConstants.SAME_AS_PREVIOUS_PASSWORD_MSG);
		
		authenticationService.updatePassword("NotAman", "Abc12345", "Abc12345");
	}
	
	@Test
	public void shouldSuccessfullyChangeOldPasswordToNewPasswordWhenUpdatingPassword(){
		
		authenticationService.addNewUser("NotAman", "Abc12345");
		
		String result = authenticationService.updatePassword("NotAman", "Abc12345", "Bcd23456");
		assertEquals(ApplicationConstants.SUCCESSFULLY_UPDATED_PASSWORD_MSG, result);
	}
	
	@Test
	public void shouldThrowExceptionOfIncorrectPasswordWhenUpdatingPassword(){
		
		authenticationService.addNewUser("NotAman", "Abc12345");
		
		expected.expect(RuntimeException.class);
		expected.expectMessage(ApplicationConstants.INVALID_PASSWORD_MSG);
		
		authenticationService.updatePassword("NotAman", "Abc12395", "Bcd23456");
	}
	
	@Test
	public void shouldThrowExceptionOfInvalidUserWhenUpdatingPassword(){
		
		authenticationService.addNewUser("NotAman", "Abc12345");
		
		expected.expect(RuntimeException.class);
		expected.expectMessage(ApplicationConstants.INVALID_USERNAME_MSG);
		
		authenticationService.updatePassword("Aman", "Abc12395", "Bcd23456");
		
	}
}
