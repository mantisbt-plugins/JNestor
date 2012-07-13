package com.supermanhamuerto.nestor;

import java.util.List;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.jsoup.nodes.Element;

public class MailChunkAlternative implements MailChunk
{
	private static Log log = LogFactory.getLog( MailChunkAlternative.class );
			
	String htmlContent;
	String textContent;
	
	public void init()
	{
		htmlContent = "";
		textContent = "";
	}
	
	@Override
    public boolean isHtml()
    {
		return !htmlContent.isEmpty();
    }

	@Override
    public boolean isText()
    {
	    return !textContent.isEmpty();
    }

	@Override
    public boolean isAttachment()
    {
	    return false;
    }

	@Override
    public void setMultipartContent(MimeMultipart mp)
    {
		if( mp != null )
		{
			try
			{
				// for every part, detect the html and the text part 
				for( int nPart = 0; nPart < mp.getCount(); nPart++ )
				{
					BodyPart part = mp.getBodyPart(nPart); 
					
					if( part.getContentType().matches( "(?i)(?s)text/html;.*" ) )
					{
						htmlContent = part.getContent().toString();
					}
					if( part.getContentType().matches( "(?i)(?s)text/plain;.*" ) )
					{
						textContent = part.getContent().toString();
					}
				} // for
				
			}catch( Exception e )
			{
				log.error( e );
				log.error( "Ignoring error and continue" );
			}

		} // mp != null
	    
    }

	@Override
    public void setContent(String content)
    {
	    textContent = content;	    
    }

	@Override
    public void setAttachment(DataHandler dh, int size, String filename, String contentType)
    {
		// do nothing
    }

	@Override
    public String getAsText()
    {
	    return textContent;
    }

	@Override
    public String getAsHtml()
    {
		if( htmlContent == null )
		{
			// delete all the <meta content=".....">
			Document doc = Jsoup.parse( htmlContent ); 
			List<Element> elements = doc.head().select("meta[content]");
			
			while( elements.size() > 0 )
			{
				elements.remove( 0 );
			}
			
			htmlContent = doc.toString();
			
			htmlContent = Jsoup.clean( htmlContent, Whitelist.relaxed() );
			
		} // simpleHtmlContent == null
		return htmlContent;
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
