package com.supermanhamuerto.nestor.mantis;

public class NoteData
{
	private long id; 
	private boolean priv; 
	private String text;
	
	public NoteData()
	{
		id = 0L; 
		priv = false; 
		text = "";
	}
	
	public long getId()
	{
		return id; 
	}
	public void setId( long id )
	{
		this.id = id; 
	}
	
	public boolean isPriv()
    {
    	return priv;
    }
	public void setPriv(boolean priv)
    {
    	this.priv = priv;
    }
	public String getText()
    {
    	return text;
    }
	public void setText(String text)
    {
    	this.text = text;
    }

} // NoteData
