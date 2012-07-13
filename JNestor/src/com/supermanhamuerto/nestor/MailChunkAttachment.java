package com.supermanhamuerto.nestor;

import javax.activation.DataHandler;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MailChunkAttachment implements MailChunk
{
	private static Log log = LogFactory.getLog(MailChunkAttachment.class);

	private byte[] attachment;
	private int attachmentSize;
	private String attachmentFilename;
	private String attachmentContentType;
	private String attachmentDisposition;
	private int maximumAttachmentSize;	
	private DataHandler dh; 
	
	public int getMaximumAttachmentSize()
    {
    	return maximumAttachmentSize;
    }

	public void setMaximumAttachmentSize(int maximumAttachmentSize)
    {
    	this.maximumAttachmentSize = maximumAttachmentSize;
    }
	
	@Override
	public boolean isHtml()
	{
		return false;
	}

	@Override
	public boolean isText()
	{
		return false;
	}

	@Override
	public boolean isAttachment()
	{
		return true;
	}


	@Override
	public void setAttachment( DataHandler dh, int size, String filename, String contentType )
	{
		this.dh = dh; 
		
		if( size < maximumAttachmentSize )
		{
			attachment = new byte[size];

			try
			{
				size = dh.getInputStream().read( attachment, 0, size );

			} catch( Exception e )
			{
				
				log.error( "Error reading attachment" );
				log.error( "Filename: " + filename );
				log.error( "Size: " +  size );
				log.error( e );
				log.error( "Ignoring error...");
			}
			finally
			{
				try{ dh.getInputStream().close(); }catch( Exception e ){};
			}

			attachmentSize = size; 
			attachmentFilename = filename;
			attachmentContentType = contentType;
			
			log.info( "Attachment");
			log.info( "Filename: " + attachmentFilename );
			log.info( "Size: " +  attachmentSize );
			log.info( "Content Type: " + attachmentContentType );
			
		} // size < maximumAttachmentSize
	} // setAttachment
	
	@Override
	public void setMultipartContent( MimeMultipart mp )
	{
		// do nothing 
	}
	
	@Override
	public void setContent( String content )
	{
		// do nothing 
	}

	@Override
	public String getAsText()
	{
		return null;
	}

	@Override
	public String getAsHtml()
	{
		return null;
	}

	@Override
	public String getAsSimpleHtml()
	{
		return null;
	}
	
	
	@Override
	public byte[] getAttachment()
	{
		return attachment;
	}
	
	@Override
	public int getAttachmentSize()
	{
		return attachmentSize;
	}
	
	@Override
	public String getAttachmentFilename()
	{
		return attachmentFilename;
	}


	@Override
	public String getAttachmentContentType()
	{
		return attachmentContentType;
	}

	@Override
	public String getAttachmentDisposition()	
	{
		return attachmentDisposition;
	}
	
	@Override
	public DataHandler getAttachmentDataHandler()
	{
		return dh;
	}

}
