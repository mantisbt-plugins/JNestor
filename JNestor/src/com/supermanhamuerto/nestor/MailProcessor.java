package com.supermanhamuerto.nestor;

import java.util.Properties;

import javax.mail.Session;

public interface MailProcessor
{
	public Session getSession();
	
	public void setSession( Session newSession );
	
	public Properties getCriteria();
	
	public void setCriteria( Properties newCriteria );

	public boolean matchCriteria( MailMessage msg );
	
	public void process( MailMessage msg );
	
}
