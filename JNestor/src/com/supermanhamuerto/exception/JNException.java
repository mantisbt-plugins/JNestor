package com.supermanhamuerto.exception;

public class JNException extends Exception
{
	/**
     * 
     */
    private static final long serialVersionUID = -3816791227736386183L;

	public JNException( String message )
	{
		super(message);
	}
	
	public JNException( Exception e )
	{
		super( e ); 
	}
}
