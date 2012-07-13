package com.supermanhamuerto.nestor.mantis;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class IssueDataFactory implements ApplicationContextAware
{
	ApplicationContext ctx;
	
	@Override
    public void setApplicationContext(ApplicationContext ctx)
            throws BeansException
    {
		this.ctx = ctx;
    }
	
	public IssueDataDto getIssueData()
	{
		return (IssueDataDto) ctx.getBean( "issueData" );
	} // getIssueData
	
	public IssueDataAttachment getIssueDataAttachment()
	{
		return (IssueDataAttachment) ctx.getBean( "issueDataAttachment" );
	} // getIssueDataAttachment

	public NoteData getNoteData()
	{
		return (NoteData) ctx.getBean( "noteData" ); 
	} // getNoteData
	
}
