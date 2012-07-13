package com.supermanhamuerto.nestor.mantis;

import javax.mail.Flags.Flag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.supermanhamuerto.exception.JNException;
import com.supermanhamuerto.nestor.MailMessage;

public class MantisProcessorActionDeleter implements MantisProcessorAction
{
	private static Log log = LogFactory.getLog(MantisProcessorActionDeleter.class);
	
	@Override
	public void doAction(long issueId, MailMessage msg, boolean exists) throws JNException
	{
		try
		{
			msg.getMessage().setFlag(Flag.DELETED, true);
			
			// open the current folder the mail message belongs to 
			// and do an Expunge
			msg.getMessage().getFolder().expunge();
			
		}
		catch( Exception e )
		{
			log.error( e );
			throw new JNException( e );
		}
		

	} // doAction
	
}
