package com.supermanhamuerto.nestor;

import javax.mail.Message;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class MailMessageFactory implements ApplicationContextAware
{
	ApplicationContext ctx; 
	
	public MailMessage readMessage( Message msg )
	{
		MailMessage result = (MailMessage) ctx.getBean( "mailMessage" );
		
		result.readMessage( msg ); 
		
		return result;
		
	} // getMessage

	@Override
    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException
    {
		this.ctx = ctx;
    }

}
