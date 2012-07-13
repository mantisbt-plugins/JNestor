package com.supermanhamuerto.nestor.mantis;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Store;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.supermanhamuerto.exception.JNException;
import com.supermanhamuerto.nestor.MailMessage;
import com.supermanhamuerto.nestor.MailSessionFactory;

public class MantisProcessorActionArchive implements MantisProcessorAction
{
	private static Log log = LogFactory.getLog(MantisProcessorActionArchive.class);
	
	private MailSessionFactory msf;
	
	private String fromFolder;
	private String toFolder;

	/**
	 * Archive the message in the designated folder
	 */
	@Override
	public void doAction(long issueId, MailMessage msg, boolean exists) throws JNException
	{
		try
		{
			Store store = msf.getStoreInstance();

			// first, copy the messages from 
			// the source folder to the destination
			// folder
			//Folder fromFolderObject = store.getFolder( fromFolder );
			Folder fromFolderObject = msg.getMessage().getFolder();
			Folder toFolderObject = store.getFolder( toFolder );
			
			//fromFolderObject.open(Folder.READ_WRITE);
			toFolderObject.open(Folder.READ_WRITE);
			
			fromFolderObject.copyMessages( new Message[] {msg.getMessage()}, toFolderObject );
			
			// next, delete the message from the 
			// source folder
			msg.getMessage().setFlag(Flags.Flag.DELETED,true);
			
			fromFolderObject.expunge();
			//fromFolderObject.close( true ); 
			toFolderObject.close( true );
			
		}
		catch( Exception e )
		{
			log.error( e );
			throw new JNException( e );
		}
	}

	public MailSessionFactory getMailSessionFactory()
	{
		return this.msf;
	}
	public void setMailSessionFactory( MailSessionFactory msf )
	{
		this.msf = msf;
	}
	
	public String getFromFolder()
    {
    	return fromFolder;
    }

	public void setFromFolder(String fromFolder)
    {
    	this.fromFolder = fromFolder;
    }

	public String getToFolder()
	{
		return toFolder;
	}
	public void setToFolder( String toFolder )
	{
		this.toFolder = toFolder;
	}
	
}
