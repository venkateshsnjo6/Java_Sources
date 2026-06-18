package com.java.javasecurity.query;

import org.springframework.stereotype.Component;

@Component
public class UserDetailsQuery {

	public String getUserDetails() {
		StringBuilder builder = new StringBuilder();
		builder.append("select name,password from userdetails \n");
		builder.append("where name=?;");
		return builder.toString();
	}

}
