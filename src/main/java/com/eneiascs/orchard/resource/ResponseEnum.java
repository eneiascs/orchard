package com.eneiascs.orchard.resource;

public enum ResponseEnum {
	EMAIL_SENT("Email sent"),
	USER_NOT_FOUND("User not found"), 
	PASSWORD_NOT_MATCHED("Password not matched"), 
	USER_PICTURE_SAVED("User picture saved"), 
	USER_PICTURE_NOT_SAVED("User picture not saved"), 
	ERROR("An error has occured"),
	USERNAME_EXISTS("Username exists"),
	EMAIL_EXISTS("Email exists"),
	NO_USER_FOUND("No user found"),
	NO_USERS_FOUND("No users found"), 
	POST_NOT_FOUND("Post not found"), 
	POST_LIKED("Post liked"),
	POST_UNLIKED("Post unliked"), 
	COMMENT_NOT_ADDED("Comment not added");
	
	
	private String message;

	ResponseEnum(String message) {
		this.setMessage(message);
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
