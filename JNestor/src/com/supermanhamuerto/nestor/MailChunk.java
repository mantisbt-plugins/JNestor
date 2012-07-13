package com.supermanhamuerto.nestor;

import javax.activation.DataHandler;
import javax.mail.internet.MimeMultipart;

public interface MailChunk
{
	public boolean isHtml(); 
	public boolean isText();
	public boolean isAttachment();
	public void setMultipartContent( MimeMultipart mp );
	public void setContent( String content );
	public void setAttachment( DataHandler dh, int size, 
							   String filename, String contentType );
	public String getAsText();
	public String getAsHtml();
	// Simple HTML: only the following 
	// is allowed: 
	// a (links)
	// br (new line)
	// p (paragraph)
	// b (bold)
	// u (underlined)
	// pre (preformatted text)
	public String getAsSimpleHtml();
	public byte[] getAttachment();
	public int getAttachmentSize();
	public String getAttachmentFilename();
	public String getAttachmentContentType();
	public String getAttachmentDisposition();
	public DataHandler getAttachmentDataHandler();
}
