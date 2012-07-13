package com.supermanhamuerto.nestor;

import java.util.Date;
import java.util.List;

import javax.mail.Message;

public interface MailMessage
{

	public void init();

	public void setMailChunkFactory(MailChunkFactory chunkFactory);

	public MailChunkFactory getMailChunkFactory();
	
	public List<MailChunk> getAllChunks();

	public void readMessage(Message msg); // readMessage

	public Message getMessage();

	public void setMessage(Message msg);

	public String getId();
	
	/**
	 * Get the address of the from field, i.e.
	 * john.doe@example.com
	 * 
	 * @return
	 */
	public String getFrom();

	/**
	 * Get full address with name, i.e.
	 * John Doe <john.doe@example.com>
	 * 
	 * @return
	 */
	public String getFromWithName();

	public String getToWithName();

	public String getTo();

	public String getSentDate();
	
	public Date getSentDateAsDate();

	public String getSubject(); // getSubject

	// sets a new body who replaces the old body
	public void setTextBody( String newBody );
	
	//
	// rules for getting the "body" of a email message: 
	//  - if there is a part identified as text, return it
	//  - if in the same multipart is  part identified as 
	//    html, return it filtered as the "good" text
	//  - the rest of the parts identified as images, other 
	//    type of data, will be accessed as attachments
	//
	public String getBodyAsText(); // getBodyAsText

	// get only the first chunk of text 
	// of the message
	public String getFirstTextChunk(); // getFirstTextChunk

	// get only the first chunk of text 
	// of the message
	public String getFirstHtmlChunk(); // getFirstHtmlChunk

	public String getBodyAsHtml(); // getBodyAsHtml

	public String getBodyAsSimpleHtml(); // getBodyAsSimpleHtml

	
}