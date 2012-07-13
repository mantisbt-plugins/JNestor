package com.supermanhamuerto.nestor;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class MailChunkFactory implements ApplicationContextAware
{
	private ApplicationContext ctx;
	
	@Override
    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException
    {
	    this.ctx = ctx;
    }

	public MailChunk getHtmlChunk()
	{
		return (MailChunk) ctx.getBean("mailChunkHtml");
	}
	
	public MailChunk getTextChunk()
	{
		return (MailChunk) ctx.getBean("mailChunkText");
	}
	
	public MailChunk getAlternativeChunk()
	{
		return (MailChunk) ctx.getBean("mailChunkAlternative");
	}
	
	public MailChunk getAttachmentChunk()
	{
		return (MailChunk) ctx.getBean("mailChunkAttachment");
	}
}
