package com.supermanhamuerto.nestor.mantis;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class IssueDataDto
{
	@SuppressWarnings("unused")
    private static Log log = LogFactory.getLog(IssueDataDto.class);

	private long id;
	private String category;
	private String reproducibillity;
	private String severity;
	private String priority;
	private String assignTo;
	private String summary;
	private String description;
	private String stepsToReproduce;
	private String additionalInformation;
	private List<IssueDataAttachment> attachments;
	
	public void init()
	{
		attachments = new ArrayList<IssueDataAttachment>();
	}

	public boolean addAttachment( IssueDataAttachment att )
	{
		return attachments.add( att );
	}
	public List<IssueDataAttachment> getAttachments()
	{
		return this.attachments;
	}
	public void setAttachments( List<IssueDataAttachment> attachments )
	{
		this.attachments = attachments;
	}
	
	public long getId()
	{
		return id;
	}
	public void setId( long newId )
	{
		this.id = newId;
	}
	public String getCategory()
    {
    	return category;
    }

	public void setCategory(String category)
    {
    	this.category = category;
    }

	public String getReproducibillity()
    {
    	return reproducibillity;
    }

	public void setReproducibillity(String reproducibillity)
    {
    	this.reproducibillity = reproducibillity;
    }

	public String getSeverity()
    {
    	return severity;
    }

	public void setSeverity(String severity)
    {
    	this.severity = severity;
    }

	public String getPriority()
    {
    	return priority;
    }

	public void setPriority(String priority)
    {
    	this.priority = priority;
    }

	public String getAssignTo()
    {
    	return assignTo;
    }

	public void setAssignTo(String assignTo)
    {
    	this.assignTo = assignTo;
    }

	public String getSummary()
    {
    	return summary;
    }

	public void setSummary(String summary)
    {
    	this.summary = summary;
    }

	public String getDescription()
    {
    	return description;
    }

	public void setDescription(String description)
    {
    	this.description = description;
    }

	public String getStepsToReproduce()
    {
    	return stepsToReproduce;
    }

	public void setStepsToReproduce(String stepsToReproduce)
    {
    	this.stepsToReproduce = stepsToReproduce;
    }

	public String getAdditionalInformation()
    {
    	return additionalInformation;
    }

	public void setAdditionalInformation(String additionalInformation)
    {
    	this.additionalInformation = additionalInformation;
    }
	
}
