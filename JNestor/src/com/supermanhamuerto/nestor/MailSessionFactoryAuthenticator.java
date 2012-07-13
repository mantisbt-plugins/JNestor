package com.supermanhamuerto.nestor;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MailSessionFactoryAuthenticator extends Authenticator
{
	private String username;
	private String password;
	
	protected PasswordAuthentication getPasswordAuthentication()
	{
		return new PasswordAuthentication( username, password );
	} // getPasswordAuthentication

	public String getUsername()
    {
    	return username;
    }

	public void setUsername(String username)
    {
    	this.username = username;
    }

	public String getPassword()
    {
    	return password;
    }

	public void setPassword(String password)
    {
    	this.password = password;
    }
}
