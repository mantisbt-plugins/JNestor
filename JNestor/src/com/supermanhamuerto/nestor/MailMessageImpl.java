package com.supermanhamuerto.nestor;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.supermanhamuerto.util.Md5;

/**
 * Helper object to process the content of the mail 
 * message. It contains methods to scan all the 
 * content of the body, process the attachments, 
 * identify the mail addresses and things like this
 * 
 * @author rluna
 *
 */
public class MailMessageImpl implements MailMessage
{
	private static Log log = LogFactory.getLog(MailMessage.class);
	
	private MailChunkFactory chunkFactory;
	

	private Message msg; 
	private String subject;
	// all the parts and multiparts and etc 
	// will be transformed into chunks, 
	// more affordable and easy to use 
	private List<MailChunk> allChunks;
	// body, as text (not html)
	private String newBody;
	
	
	@Override
    public void init()
	{
		allChunks = null;
		newBody = null;
	}
	
	@Override
    public void setMailChunkFactory( MailChunkFactory chunkFactory )
	{
		this.chunkFactory = chunkFactory;
	}
	
	@Override
    public MailChunkFactory getMailChunkFactory()
	{
		return this.chunkFactory;
	}

	
	@Override
    public List<MailChunk> getAllChunks()
	{
		return this.allChunks;
	}
	
	@Override
    public void readMessage( Message msg )
	{
		setMessage( msg );
	} // readMessage

	@Override
    public Message getMessage()
	{
		return this.msg;
	}
	
	@Override
    public void setMessage( Message msg )
	{
		this.msg = msg;
		try
		{
			subject = msg.getSubject();
			log.info( subject );
			allChunks = new ArrayList<MailChunk>();
			if( msg.isMimeType( "text/plain" ) )
			{
				MailChunk ch = chunkFactory.getTextChunk();
				ch.setContent( msg.getContent().toString() );
				allChunks.add( ch );
			}
			else if( msg.isMimeType( "text/html") )
			{
				MailChunk ch = chunkFactory.getHtmlChunk();
				ch.setContent( msg.getContent().toString() );
				allChunks.add( ch );				
			}
			else
			{
				MimeMultipart bodyPart = (MimeMultipart) msg.getContent();
				parseMultipart( bodyPart );				
			}
		}
		catch( Exception e )
		{
			// log the error
			// and ignore 
			log.error( e );
			if( subject == null || subject.isEmpty() )
			{
				subject = "No subject found";
			} // subject == null 
		} // catch
	}

	@Override
	public String getId()
	{
		return MailMessageImpl.getMessageId( msg );
	}
	
	public static String getMessageId( Message m )
	{
		String out = null;
		try
		{
			out = m.getHeader("Message-ID")[0];
		}
		catch( MessagingException e )
		{
			// if the header is not found, calculate 
			// an id based on the body
			try
			{
				StringBuilder sb = new StringBuilder();
				
				sb.append( m.getSentDate().toString() );
				sb.append( " " );
				for( int nFrom = 0; nFrom < m.getFrom().length; nFrom++ )
				{
					sb.append( m.getFrom()[nFrom] );
					sb.append( " " );
				}
				
				out = Md5.hash( sb.toString() );
			}
			catch( Exception e2 )
			{
				// do nothing, ignore error
			}
		}
		
		return out; 
	} // getId()

	@Override
    public String getFrom()
	{
		String out = "";
		try
		{
			Address[] from = msg.getFrom();
			if( from.length > 0 )
			{
				InternetAddress ia = (InternetAddress) from[0];
				out = ia.getAddress();
			} // from.length > 0 
		}
		catch( MessagingException e )
		{
			// ignore the error
		}
		return out;
	}

	@Override
    public String getFromWithName()
	{
		String out = "";
		try
		{
			Address[] from = msg.getFrom();
			if( from.length > 0 )
			{
				InternetAddress ia = (InternetAddress) from[0];
				out = ia.toUnicodeString();
			} // from.length > 0 
		}
		catch( MessagingException e )
		{
			// ignore the error
		}
		return out;
	}
	
	/* (non-Javadoc)
     * @see com.supermanhamuerto.nestor.MailMessage#getToWithName()
     */
	@Override
    public String getToWithName()
	{
		String out = "";
		try
		{
			Address[] recipients = msg.getRecipients( Message.RecipientType.TO );
			for( int ad = 0; ad < recipients.length; ad++ )
			{
				InternetAddress ia = (InternetAddress) recipients[ad];
				out = out.concat( ia.toUnicodeString() + ", " );
			} // for 
		}
		catch( MessagingException e )
		{
			// ignore the error
		}
		return out;
	}
	
	/* (non-Javadoc)
     * @see com.supermanhamuerto.nestor.MailMessage#getTo()
     */
	@Override
    public String getTo()
	{
		String out = "";
		try
		{
			Address[] recipients = msg.getRecipients( Message.RecipientType.TO );
			for( int ad = 0; ad < recipients.length; ad++ )
			{
				InternetAddress ia = (InternetAddress) recipients[ad];
				out = out.concat( ia.getAddress() + ", " );
			} // for 
		}
		catch( MessagingException e )
		{
			// ignore the error
		}
		return out;
	}
	

	/* (non-Javadoc)
     * @see com.supermanhamuerto.nestor.MailMessage#getSentDate()
     */
	@Override
    public String getSentDate()
	{
		String out;
		try
		{
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			out = df.format( msg.getSentDate() );
		}
		catch( MessagingException e )
		{
			// ignore the error
			out = "";
		}
		return out;
	}
	
	@Override
	public Date getSentDateAsDate()
	{
		Date out;
		try
		{
			out = msg.getSentDate();
		}
		catch( MessagingException e )
		{
			// ignore the error 
			out = null; 
		}
		return out; 
	}
	
	/* (non-Javadoc)
     * @see com.supermanhamuerto.nestor.MailMessage#getSubject()
     */
	@Override
    public String getSubject()
	{
		return subject;
	} // getSubject
	
	private void parseText( BodyPart p ) throws MessagingException, IOException
	{
		MailChunk ch = chunkFactory.getTextChunk();
		ch.setContent( p.getContent().toString() );
		allChunks.add( ch );
	}
	
	private void parseMultipart( MimeMultipart mp ) throws MessagingException, IOException
	{

		// for every element in the multipart...
		for( int nPart = 0; nPart < mp.getCount(); nPart++ )
		{
			BodyPart p = mp.getBodyPart( nPart );
			
			// FIRST: treat the multipart case, but alternative 
			if( p.isMimeType( "multipart/alternative" ) )
			{
				MailChunk ch = chunkFactory.getAlternativeChunk();
				ch.setMultipartContent( (MimeMultipart) p.getContent() );
				allChunks.add( ch );
			}
			
			// SECOND: treat the multipart case, in general:
			// multipart/mised, multipart/something
			// else if( p.getContentType().matches( "(?i)(?s)multipart/mixed;.*" ) )
			else if( p.isMimeType( "multipart" ) 
						|| p.getContentType().matches( "(?i)(?s)multipart/.*" ) )
			{
				// there is a multipart; it contains 
				// other parts 
				MimeMultipart subMp = (MimeMultipart) p.getContent();
				parseMultipart( subMp );
			} // multipart/mixed
			
						
			// SECOND: attachments 
			else if( p.getDisposition() != null 
			 && p.getDisposition().equalsIgnoreCase( Part.ATTACHMENT ) )
			{
				// THIRD: the remaining cases are for attachments
				// if it is and attachment, binary
				MailChunk ch = chunkFactory.getAttachmentChunk();
				ch.setAttachment(p.getDataHandler(), 
								 p.getSize(), 
								 p.getFileName(),
								 p.getContentType() );
				allChunks.add( ch );
			} // p.getDisposition... ATTACHMENT
			
			// THIRD: text/html, not ATTACHMENT
			else if( p.isMimeType( "text/html") )
			{
				// if it is HTML...
				MailChunk ch = chunkFactory.getHtmlChunk();
				ch.setContent( p.getContent().toString() );
				allChunks.add( ch );
			} // text/html
			
			// FOURTH: text/plain, not ATTACHMENT
			else if( p.isMimeType( "text/plain") )
			{
				// if it is text...
				parseText( p );
			} // text/plain
			else
			{
				// EVERYTHING ELSE; not ATTACHMENT: treat it as attachment 
				MailChunk ch = chunkFactory.getAttachmentChunk();
				ch.setAttachment(p.getDataHandler(), 
								 p.getSize(), 
								 p.getFileName(), 
								 p.getContentType() );
				allChunks.add( ch );				
			}
						
		} // for						

	}

	@Override
	public void setTextBody( String newBody )
	{
		this.newBody = newBody;
	}
	
	//
	// rules for getting the "body" of a email message: 
	//  - if there is a part identified as text, return it
	//  - if in the same multipart is  part identified as 
	//    html, return it filtered as the "good" text
	//  - the rest of the parts identified as images, other 
	//    type of data, will be accessed as attachments
	//
	/* (non-Javadoc)
     * @see com.supermanhamuerto.nestor.MailMessage#getBodyAsText()
     */
	@Override
	public String getBodyAsText()
	{
		if( newBody == null )
		{
			// get the body from the message
			
			// detect the first text-alike body and return it
			StringBuilder body = new StringBuilder();
			Iterator<MailChunk> itChunk = allChunks.iterator();
			while( itChunk.hasNext() )
			{
				MailChunk ch = itChunk.next();

				if( !ch.isAttachment() )
					body.append( ch.getAsText() );
				
			} // itChunk.hasNext
			
			return body.toString();			
			
		}
		else
		{
			// return the modified body
			return this.newBody;
		}

	} // getBodyAsText
	
	// get only the first chunk of text 
	// of the message
	/* (non-Javadoc)
     * @see com.supermanhamuerto.nestor.MailMessage#getFirstTextChunk()
     */
	@Override
	public String getFirstTextChunk()
	{
		if( newBody == null )
		{
			// return the first text chunk of the body 
			boolean found = false; 
			StringBuilder body = new StringBuilder();
			Iterator<MailChunk> itChunk = allChunks.iterator();
			while( !found && itChunk.hasNext() )
			{
				MailChunk ch = itChunk.next();
				
				if( ch.isText() )
				{
					body.append( ch.getAsText() );
					found = true;
				} // ch.isText()
			} // while
			
			return body.toString();
		}
		else
		{
			// return the modified body
			return this.newBody;
		}
		
	} // getFirstTextChunk
	

	// get only the first chunk of text 
	// of the message
	/* (non-Javadoc)
     * @see com.supermanhamuerto.nestor.MailMessage#getFirstHtmlChunk()
     */
	@Override
    public String getFirstHtmlChunk()
	{
		if( newBody == null )
		{
			// return the first html chunk of the body
			boolean found = false; 
			StringBuilder body = new StringBuilder();
			Iterator<MailChunk> itChunk = allChunks.iterator();
			while( !found && itChunk.hasNext() )
			{
				MailChunk ch = itChunk.next();
				
				if( ch.isText() )
				{
					body.append( ch.getAsHtml() );
					found = true;
				} // ch.isText()
			} // while
			
			return body.toString();

		}
		else
		{
			// return the modified body
			return this.newBody;
		}
		
	} // getFirstHtmlChunk
	
	
	
	/* (non-Javadoc)
     * @see com.supermanhamuerto.nestor.MailMessage#getBodyAsHtml()
     */
	@Override
    public String getBodyAsHtml()
	{
		// TODO: IMPLEMENT SUPPORT FOR newBody
		
		// compose a whole text with all the text-alike
		// texts that are in the message
		StringBuilder body = new StringBuilder();
		Iterator<MailChunk> itChunk = allChunks.iterator();
		while( itChunk.hasNext() )
		{
			MailChunk ch = itChunk.next();
			
			if( !ch.isAttachment() )
				body.append( ch.getAsHtml() );
			
		} // itChunk.hasNext
		
		return body.toString();
	} // getBodyAsHtml
	

	/* (non-Javadoc)
     * @see com.supermanhamuerto.nestor.MailMessage#getBodyAsSimpleHtml()
     */
	@Override
    public String getBodyAsSimpleHtml()
	{
		// TODO: IMPLEMENT SUPPORT FOR newBody
		
		// detect the first text-alike body and return it
		StringBuilder body = new StringBuilder();
		Iterator<MailChunk> itChunk = allChunks.iterator();
		while( itChunk.hasNext() )
		{
			MailChunk ch = itChunk.next();

			if( !ch.isAttachment() )
				body.append( ch.getAsSimpleHtml() );
			
		} // itChunk.hasNext
		
		return body.toString();
	} // getBodyAsSimpleHtml

} // MailMessage


