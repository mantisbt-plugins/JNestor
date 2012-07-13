package com.supermanhamuerto.nestor;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Session;
import javax.mail.Store;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MailSessionFactory
{
	private static Log log = LogFactory.getLog(MailSessionFactory.class);

//	private static Session session = null;
	
	private boolean useSSL = false; 
	private boolean debug = true; 
	
	private Properties mailConfig;
	private Authenticator mailAuthenticator;
	
	public Authenticator getMailAuthenticator()
	{
		return mailAuthenticator;
	}
	public void setMailAuthenticator( Authenticator mailAuthenticator )
	{
		this.mailAuthenticator = mailAuthenticator;
	}
	
	public Properties getMailConfig()
    {
    	return mailConfig;
    }
	public void setMailConfig(Properties mailConfig)
    {
    	this.mailConfig = mailConfig;
    	// cache some values
    	cacheValues();
    }	

	
	private void cacheValues()
	{
		useSSL = Boolean.parseBoolean( mailConfig.getProperty("mail.usessl","false") );
		debug = Boolean.parseBoolean( mailConfig.getProperty("debug","false") );
	}
	
	public Session getInstance()
	{
		Session session;
		
		if( useSSL )
		{
			session = Session.getInstance( mailConfig, mailAuthenticator );
		}
		else
		{
			session = Session.getInstance( mailConfig );
		}
		
		session.setDebug( debug );

		log.info( "    User: " + getConfig("mail.user") );
		if( getConfig( "mail.password").isEmpty() )
			log.info( "Password: empty" );
		else
			log.info( "Password: ********" );
		log.info( "    Host: " + getConfig("mail.store.host") );
		log.info( "    Port: " + getConfig("mail.imap.port") );
			
		return session;
	}
	
	
	
	public Store getStoreInstance()
	{
		Store store = null; 
		
		try
		{
			Session session = this.getInstance();
			
			store = session.getStore( getConfig("mail.store.protocol","imap") );
			
			store.connect( getConfig("mail.store.host"),
					       getConfig("mail.user"),
					       getConfig("mail.password") );
		}catch( Exception e )
		{
			log.error( e );
			try
			{
				store.close();
			}
			catch( Exception e2 )
			{
				log.error( "When trying to close the session after the error I've got an error also:");
				log.error( e2 );
			}
		}

		return store;
		
	}
	
	
	public String getConfig( String key )
	{
		return mailConfig.getProperty( key );
	}
	
	public String getConfig( String key, String defaultValue )
	{
		return mailConfig.getProperty( key, defaultValue );
	}
	
}
