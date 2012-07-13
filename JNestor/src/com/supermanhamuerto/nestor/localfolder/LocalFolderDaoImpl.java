package com.supermanhamuerto.nestor.localfolder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.supermanhamuerto.nestor.MailMessage;
import com.supermanhamuerto.nestor.entity.MessageEnt;

public class LocalFolderDaoImpl implements LocalFolder
{
	private static Log log = LogFactory.getLog( LocalFolderDaoImpl.class );
	private SessionFactory sessionFactory; 
	private Session ses;
	
	
	public void setSessionFactory( SessionFactory sessionFactory )
	{
		this.sessionFactory = sessionFactory; 
		this.ses = sessionFactory.openSession();
	}
	public SessionFactory getSessionFactory()
	{
		return this.sessionFactory;
	}
	
	/* (non-Javadoc)
     * @see com.supermanhamuerto.nestor.localfolder.LocalFolder#messageExist(com.supermanhamuerto.nestor.MailMessage)
     */
	@Override
    public boolean messageExist( MailMessage msg )
	{
		return messageExist( msg.getId() );
	}
	
	@Override
	public boolean messageExist(String id)
	{
		// in case of error, the message is declared as "existent"
		// to avoid entering the same message again and again
		boolean result = true;
		
		try
		{
			StringBuilder hql = new StringBuilder();
			
			hql.append( "from MessageEnt " );
			hql.append( "where messageId = :messageId " );
			
			Query q = ses.createQuery( hql.toString() );
			
			q.setParameter( "messageId", id ); 
			
			result = !q.list().isEmpty();
			
		}
		catch( Exception e )
		{
			log.error( e ); 
		}

		return result;
	}

	/* (non-Javadoc)
     * @see com.supermanhamuerto.nestor.localfolder.LocalFolder#saveMessage(com.supermanhamuerto.nestor.MailMessage)
     */
	@Override
    public void saveMessage( MailMessage msg )
	{
		MessageEnt msgEntity = new MessageEnt(); 
		
		Transaction tx = ses.beginTransaction();
		
		msgEntity.setMessageId( msg.getId() );
		msgEntity.setSent( msg.getSentDateAsDate() );
		msgEntity.setMsgFrom( msg.getFrom() );
		msgEntity.setMsgTo( msg.getTo() );
		msgEntity.setSubject( msg.getSubject() );
		msgEntity.setBody( msg.getBodyAsText() );
		ses.saveOrUpdate( msgEntity );
		
		tx.commit();
	}

	@Override
	public void deleteMessage( MailMessage msg )
	{
		MessageEnt msgEntity = new MessageEnt(); 
		
		Transaction tx = ses.beginTransaction();
		
		msgEntity.setMessageId( msg.getId() );
		msgEntity.setSent( msg.getSentDateAsDate() );
		msgEntity.setMsgFrom( msg.getFrom() );
		msgEntity.setMsgTo( msg.getTo() );
		msgEntity.setSubject( msg.getSubject() );
		msgEntity.setBody( msg.getBodyAsText() );
		ses.delete( msgEntity );
		
		tx.commit();
	}

	
}
