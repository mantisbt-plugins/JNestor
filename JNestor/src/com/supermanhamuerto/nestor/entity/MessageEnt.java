package com.supermanhamuerto.nestor.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table( name = "jn_message" )
public class MessageEnt implements Serializable
{
	/**
     * 
     */
    private static final long serialVersionUID = 7046221949804778109L;
    
	private String messageId;
	private Date sent; 
	private String msgFrom;
	private String msgTo;
	private String subject;
	private String body; 
	
	@Id
	@Column( name="message_id", nullable = true, length = 1000 )
	public String getMessageId()
    {
    	return messageId;
    }
	public void setMessageId(String messageId)
    {
    	this.messageId = messageId;
    }
	
	@Column( name="sent", nullable = true )
	public Date getSent()
    {
    	return sent;
    }
	public void setSent(Date sent)
    {
    	this.sent = sent;
    }
	
	@Column( name="msg_from", nullable = true, length = 1000 )
	public String getMsgFrom()
    {
    	return msgFrom;
    }
	public void setMsgFrom(String msgFrom)
    {
    	this.msgFrom = msgFrom;
    }
	
	@Column( name="msg_to", nullable = true, length = 1000 )
	public String getMsgTo()
    {
    	return msgTo;
    }
	public void setMsgTo(String msgTo)
    {
    	this.msgTo = msgTo;
    }
	
	@Column( name="subject", nullable = true )
	public String getSubject()
    {
    	return subject;
    }
	public void setSubject(String subject)
    {
    	this.subject = subject;
    }
	
	@Column( name="body", nullable = true )
	public String getBody()
    {
    	return body;
    }
	public void setBody(String body)
    {
    	this.body = body;
    }
	
}
