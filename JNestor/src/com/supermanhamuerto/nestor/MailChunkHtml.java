package com.supermanhamuerto.nestor;

import javax.activation.DataHandler;
import javax.mail.internet.MimeMultipart;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

public class MailChunkHtml implements MailChunk
{
	
	String content;
	String textContent;
	String simpleHtmlContent;

	@Override
	public void setMultipartContent( MimeMultipart mp )
	{
		try
		{
			content = mp.getBodyPart(0).getContent().toString();
		}
		catch( Exception e )
		{
			
		}
	}
	
	@Override
	public void setContent( String content )
	{
		this.content = content; 

		// the text content will be extracted
		// when it is called the "getAsText" method
		this.textContent = null;
		// content with simple html will be 
		// generated when the method getAsSimpleHtml
		// method is called
		this.simpleHtmlContent = null;
	}
	
	@Override
	public boolean isHtml()
	{
		return true;
	}

	@Override
	public boolean isText()
	{
		return false;
	}

	@Override
	public boolean isAttachment()
	{
		return false;
	}

	@Override
	public void setAttachment( DataHandler dh, int size, String filename, String contentType )
	{
		// do nothing
	}

	@Override
	public String getAsText()
	{
		if( textContent == null )
		{
			textContent = Jsoup.clean( content, Whitelist.none() );
			// transform lines with only blanks to 
			// just empty lines 
			textContent = textContent.replaceAll("\\n\\s+\\n", "\n\n");
			// erase single carriage returns
			// (I understand that single carriage returns may 
			// be a cut in a paragraph that should be sticked again)
			//content = content.replaceAll(" \\n", " ");
			// transform tripe empty lines \n\n to double line \n
			textContent = textContent.replaceAll("\\n\\n+", "\n\n");			
		}
		return textContent;
	}

	@Override
	public String getAsHtml()
	{
		return content;
	}

	// Simple HTML: only the following 
	// is allowed: 
	// a (links)
	// br (new line)
	// p (paragraph)
	// b (bold)
	// u (underlined)
	// pre (preformatted text)
	@Override
	public String getAsSimpleHtml()
	{
		if( simpleHtmlContent == null )
		{
			simpleHtmlContent = Jsoup.clean( content, Whitelist.basic() );
		} // simpleHtmlContent == null
		return simpleHtmlContent;
	}

	
	@Override
	public byte[] getAttachment()
	{
		return null;
	}
	
	@Override
	public int getAttachmentSize()
	{
		return 0;
	}
	
	@Override
	public String getAttachmentFilename()
	{
		return null;
	}

	@Override
	public String getAttachmentContentType()
	{
		return null;
	}

	@Override
	public String getAttachmentDisposition()	
	{
		return null;
	}

	@Override
	public DataHandler getAttachmentDataHandler()
	{
		return null;
	}

}
