package com.supermanhamuerto.nestor;

import javax.activation.DataHandler;
import javax.mail.internet.MimeMultipart;


public class MailChunkText implements MailChunk
{
	private String content; 
	
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
	
	// create from string 
	@Override
	public void setContent( String content )
	{
		// transform double lines to single lines:
		//  \r\n\r\n -> \r\n
		// the \s class represents the blank characters
		content = content.replaceAll("\r\n\\s*[\r\n]\\s*[\r\n]", "\r\n\r\n");
		
		this.content = content;
		
	}
	
	@Override
	public void setAttachment( DataHandler dh, int size, String filename, String contentType )
	{
		// do nothing
	}

	@Override
    public boolean isHtml()
    {
	    return false;
    }

	@Override
    public boolean isText()
    {
	    return true;
    }

	@Override
    public boolean isAttachment()
    {
	    return false;
    }

	@Override
    public String getAsText()
    {
	    return content;
    }

	@Override
    public String getAsHtml()
    {
		// to return as html: 
		// <html><body>
		// replace every \n by <br>
		// </html></body>
		
		String out = "<html><body>##content</body></html>";
	    
		out = out.replace( "##content",  content );
		out = out.replace( "\n", "<br>\n" );
		
	    return out;
    }

	@Override
	public String getAsSimpleHtml()
	{
		return getAsHtml();
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
