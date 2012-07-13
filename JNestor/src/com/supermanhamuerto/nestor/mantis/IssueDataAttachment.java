package com.supermanhamuerto.nestor.mantis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IssueDataAttachment
{
	
	private static Log log = LogFactory.getLog(IssueDataAttachment.class);
	
	private String filename;
	private String contentType;
	private byte[] content;

	public IssueDataAttachment()
	{
		
	}
	
	public IssueDataAttachment( byte[] content, String filename, String contentType )
	{
		this.content = content;
		log.debug( "I've received an attachment of " + content.length +  " size" );
		this.filename = filename;
		this.contentType = contentType;
	}
	
	public String getFilename()
    {
    	return filename;
    }
	public void setFilename(String filename)
    {
    	this.filename = filename;
    }
	public String getContentType()
    {
    	return contentType;
    }
	public void setContentType(String contentType)
    {
    	this.contentType = contentType;
    }
	public byte[] getContent()
    {
		log.debug( "Returning an attachment of " +  content.length + " size" );
    	return content;
    }
	public void setContent(byte[] content)
    {
    	this.content = content;
    }

}
