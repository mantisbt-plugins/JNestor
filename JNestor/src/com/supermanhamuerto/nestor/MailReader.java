package com.supermanhamuerto.nestor;

import java.util.Iterator;
import java.util.List;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.supermanhamuerto.nestor.localfolder.LocalFolder;
import com.supermanhamuerto.nestor.mantis.Mantis;

public class MailReader
{
	private static Log log = LogFactory.getLog(MailReader.class);
	
	private Mantis mantis;
	private Session session;
	private MailSessionFactory msf;
	private MailMessageFactory mmf;
//	private MailFilter mailFilter;
	private MailPreprocessor preProcessor;
	private List<MailProcessor> mailProcessorsList;
	private LocalFolder localFolder;
	
	
	public MailSessionFactory getMailSessionFactory()
	{
		return this.msf;
	}
	
	public void setMailSessionFactory( MailSessionFactory newMailSessionFactory )
	{
		this.msf = newMailSessionFactory;
	}
	
	public MailMessageFactory getMailMessageFactory()
	{
		return this.mmf;
	}
	public void setMailMessageFactory( MailMessageFactory mmf )
	{
		this.mmf = mmf;
	}
	
//	public MailFilter getMailFilter()
//	{
//		return mailFilter;
//	}
//	public void setMailFilter( MailFilter mailFilter )
//	{
//		this.mailFilter = mailFilter;
//	}
	
	public MailPreprocessor getPreProcessor()
    {
    	return preProcessor;
    }

	public void setPreProcessor(MailPreprocessor preProcessor)
    {
    	this.preProcessor = preProcessor;
    }

	public List<MailProcessor> getMailProcessorsList()
	{
		return this.mailProcessorsList;
	}
	public void setMailProcessorsList( List<MailProcessor> newMailProcessorsList )
	{
		this.mailProcessorsList = newMailProcessorsList;
	}

	public LocalFolder getLocalFolder()
	{
		return this.localFolder;
	}
	
	public void setLocalFolder( LocalFolder lf )
	{
		this.localFolder = lf; 
	}
	
	public void readEmails()
	{
		try
		{
			session = msf.getInstance();
			
			Store store = session.getStore( msf.getConfig("mail.store.protocol","imap") );
			
			store.connect( msf.getConfig("mail.store.host"),
					msf.getConfig("mail.user"),
					msf.getConfig("mail.password") );

			Folder folder = store.getFolder( msf.getConfig("mail.store.inbox","INBOX") );
			folder.open(Folder.READ_WRITE);
			
			log.info( "Folder name: " +  folder.getName() );
			
			// traverse all the messages in the folder
			MailMessage msgWrap;
			MailMessage preProcessedMsg; 
			
			Message [] allMessages = folder.getMessages();
			for( Message msg : allMessages )
			{
				preProcessedMsg = null;
				if( ! localFolder.messageExist( MailMessageImpl.getMessageId( msg ) ) )
				{
					msgWrap = mmf.readMessage( msg );
					// message doesn't exist in local folder: store it to 
					// local folder
					localFolder.saveMessage(msgWrap);

					// Apply custom preprocess rules that
					// convert one msgWrap read from the 
					// inbox: it allows to cut some part of 
					// the messages, or can drop some 
					// messages based on certain rule
					if( preProcessor != null )
						preProcessedMsg = preProcessor.applyCustomRules( msgWrap );
					
					if( preProcessedMsg != null )
					{
						// for every processor in the processor list, 
						// take it and process it 
						Iterator<MailProcessor> itMailProcessor = mailProcessorsList.iterator();
						
						while( itMailProcessor.hasNext() )
						{
							MailProcessor mp = itMailProcessor.next();
							
							mp.setSession( session );
							if( mp.matchCriteria( msgWrap ) )
								mp.process( msgWrap );
							
						} // itMailProcessor.hasNext()
						
					} // preProcessedMsg != null
					
				} // ! localFolder.messageExist
				
				
				
			} // for 
			
			folder.expunge();
			folder.close(true);
			
		}catch( Exception e )
		{
			//localFolder.saveMessage(msgWrap);
			
			log.error( e );
			log.error( "Mail configuration is: " );
			log.error( "debug = " + msf.getConfig("debug") );
			log.error( "mail.user = " + msf.getConfig("mail.user") );
			log.error( "mail.password = *********" );
			log.error( "mail.store.host = " + msf.getConfig("mail.store.host") );
			log.error( "mail.store.protocol = " + msf.getConfig("mail.store.protocol") );
			log.error( "mail.imap.port = " + msf.getConfig("mail.imap.port") );
			log.error( "mail.imap.socketFactory.port = " + msf.getConfig("mail.imap.socketFactory.port") );
			log.error( "mail.imap.socketFactory.class = " + msf.getConfig("mail.imap.socketFactory.class") );
		} // try Session
		
		
	} // readEmails
	
	
	public Mantis getMantis()
    {
    	return mantis;
    }
	public void setMantis(Mantis mantis)
    {
    	this.mantis = mantis;
    }

}
