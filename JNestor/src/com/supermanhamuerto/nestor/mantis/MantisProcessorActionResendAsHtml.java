package com.supermanhamuerto.nestor.mantis;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.supermanhamuerto.exception.JNException;
import com.supermanhamuerto.nestor.MailMessage;
import com.supermanhamuerto.nestor.MailSessionFactory;

public class MantisProcessorActionResendAsHtml implements MantisProcessorAction
{
	private static Log log = LogFactory.getLog(MantisProcessorActionResendAsHtml.class);

	private MailSessionFactory msf;
	
	private String resendFrom;
	private String resendTo;
	private String replyTo;
	private String subjectStamp;
	private String issueStamp;
	private String contentType;
	private String body;
	
	
	public MailSessionFactory getMailSessionFactory()
	{
		return this.msf;
	}
	public void setMailSessionFactory( MailSessionFactory msf )
	{
		this.msf = msf;
	}
	

	/**
	 * forwards the current message to the specified address 
	 * modifying the subject and adding a customized body
	 */
	@Override
	public void doAction(long issueId, MailMessage msg, boolean exists) throws JNException
	{
		try
		{
			Session session = msf.getInstance();
			
			// create a disconnected copy of the message 
			Message newMsg = new MimeMessage(session); 
			
			// Step 1: write the subject of the message
			String newIssueStamp = issueStamp; 
			
			newIssueStamp = newIssueStamp.replace( "##issueid", Long.toString(issueId) );

			newMsg.setSubject( subjectStamp + " " + newIssueStamp + " " + msg.getSubject() );
			
			if(log.isInfoEnabled())
				log.info( subjectStamp + " " + newIssueStamp + " " + msg.getSubject() );
			
			// Step 2: create the new body of the new message 
			// that will be a html message
			BodyPart myPart = new MimeBodyPart();
			
			String newBody = body; 
			newBody = newBody.replace( "##issueid", Long.toString(issueId) );
			
			// Add to the body the text of the original message
			newBody += "<hr>";
			newBody += String.format( "Subject: %s<br>", escapeHtml4( msg.getSubject() ) );
			newBody += String.format( "Date: %s<br>", escapeHtml4( msg.getSentDate() ) );
			newBody += String.format( "From: %s<br>", escapeHtml4( msg.getFromWithName() ) );
			newBody += msg.getBodyAsSimpleHtml();
			
			myPart.setContent( newBody, "text/html" );
			
			// Step 3: create a multipart to hold all the 
			// parts of the message: the new (called myPart)
			// and the parts corresponding to the original message
			Multipart body = new MimeMultipart();

			body.addBodyPart( myPart );
			
			newMsg.setContent( body );
			
			newMsg.setFrom( new InternetAddress( resendFrom ) );
			newMsg.addRecipient( Message.RecipientType.TO, new InternetAddress( resendTo ) );
			if( replyTo != null && !replyTo.isEmpty() )
			{
				String whoReply = replyTo;
				whoReply = whoReply.replace("##original_sender",msg.getFrom());
				newMsg.addHeader("Reply-to", whoReply);
			}
			
			Transport.send( newMsg );
			
		}catch(Exception e)
		{
			log.error( e );
			throw new JNException( e );
		}

	}	// doAction
	
	
	public String getResendFrom()
    {
    	return resendFrom;
    }
	public void setResendFrom(String resendFrom)
    {
    	this.resendFrom = resendFrom;
    }

	public String getResendTo()
    {
    	return resendTo;
    }
	public void setResendTo(String resendTo)
    {
    	this.resendTo = resendTo;
    }

	public String getReplyTo()
    {
    	return replyTo;
    }
	public void setReplyTo(String replyTo)
    {
    	this.replyTo = replyTo;
    }

	public String getSubjectStamp()
    {
    	return subjectStamp;
    }
	public void setSubjectStamp(String subjectStamp)
    {
    	this.subjectStamp = subjectStamp;
    }

	public String getIssueStamp()
	{
		return issueStamp;
	}
	public void setIssueStamp( String issueStamp )
	{
		this.issueStamp = issueStamp;
	}
	
	public String getBody()
    {
    	return body;
    }
	public void setBody(String body)
    {
    	this.body = body;
    }
	public String getContentType()
    {
	    return contentType;
    }
	public void setContentType(String contentType)
    {
	    this.contentType = contentType;
    }

}
