package com.supermanhamuerto.nestor.mantis;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.supermanhamuerto.exception.JNException;
import com.supermanhamuerto.nestor.MailChunk;
import com.supermanhamuerto.nestor.MailMessage;
import com.supermanhamuerto.nestor.MailProcessor;
import com.supermanhamuerto.nestor.mantis.IssueDataDto;
import com.supermanhamuerto.nestor.mantis.IssueDataAttachment;
import com.supermanhamuerto.nestor.mantis.IssueDataFactory;
import com.supermanhamuerto.nestor.mantis.NoteData;
import com.supermanhamuerto.nestor.mantis.MantisProcessorAction;



/**
 * <p>
 * Reglas de negocio que queremos instalar: </p>
 * <ul>
 * <li>adivinar la categoría del correo: eso debería 
 *   hacerse insertando un "categoryGuesser" 
 *   que haga un escaneo de la base de datos 
 *   de incidencias (palabras clave + categorías 
 *   en la que aparece). Por ejemplo, supongamos 
 *   que un correo tiene las palabras "necesito 
 *   un usuario y una contraseña"; en la base 
 *   de datos estas palabras aparecerán así: 
 * </ul>
 * <pre>  
 *   +----------------+---------+-----------+---------------+
 *   | word           | cat_ie8 | cat_other | cat_user_pass |
 *   +----------------+---------+-----------+---------------+
 *   | necesito       |         |     3     |       8       |
 *   +----------------+---------+-----------+---------------+
 *   | usuario        |         |           |       7       |
 *   +----------------+---------+-----------+---------------+
 *   | una            |    10   |    10     |      10       |
 *   +----------------+---------+-----------+---------------+
 *   | contraseña     |         |           |       7       |
 *   +----------------+---------+-----------+---------------+
 * </pre>
 * <p>
 *   Puede verse que por las palabras que aparecen la 
 *   categoría más posible es "cat_user_pass"</p>
 *   
 * 
 */
public class MailProcessorMantis implements MailProcessor
{
	private static Log log = LogFactory.getLog(MailProcessorMantis.class);

	private IssueDataFactory issueDataFactory;
	private int maximumAttachmentSize;
	
	private Session session;
	private Mantis mantis;
	private Properties criteriaList;
	private List<MantisProcessorAction> afterProcessActions;
	
	private boolean attachOriginalMessage;
	
	// template to add per issue
	// It can hold various values: 
	// ##from : from value
	// ##to   : to value
	// ##sent : sent date 
	private String newIssueHeaderTemplate;
    // template to add per existing 
    // issue. it can hold various values:
    // ##from : from address
    // ##to: to address
    // ##sent: sent date
	private String existingIssueHeaderTemplate;
	
	// something like 
	// [ns##issueid] that will
	// be added in every subject 
	// in order to identify what 
	// messages are already 
	// registered in mantis
	private String issueStamp;
	
	public void init()
	{
		
	}
	
	
	public Session getSession()
	{
		return this.session;
	}
	
	public void setSession( Session newSession )
	{
		this.session = newSession;
	}
	
	public IssueDataFactory getIssueDataFactory()
    {
    	return issueDataFactory;
    }

	public void setIssueDataFactory(IssueDataFactory issueDataFactory)
    {
    	this.issueDataFactory = issueDataFactory;
    }

	public int getMaximumAttachmentSize()
    {
    	return maximumAttachmentSize;
    }

	public void setMaximumAttachmentSize(int maximumAttachmentSize)
    {
    	this.maximumAttachmentSize = maximumAttachmentSize;
    }

	public void setMantis( Mantis mantis )
	{
		this.mantis = mantis;
	}
	
	public Mantis getMantis()
	{
		return this.mantis;
	}

	public String getNewIssueHeader()
	{
		return newIssueHeaderTemplate;
	}
	public void setNewIssueHeader( String newIssueHeaderTemplate )
	{
		this.newIssueHeaderTemplate = newIssueHeaderTemplate;
	}
	public String getExistingIssueHeader()
	{
		return this.existingIssueHeaderTemplate;
	}
	public void setExistingIssueHeader( String existingIssueHeaderTemplate )
	{
		this.existingIssueHeaderTemplate = existingIssueHeaderTemplate;
	}
	public String getIssueStamp()
	{
		return issueStamp;
	}
	public void setIssueStamp( String issueStamp )
	{
		this.issueStamp = issueStamp;
	}
	
	@Override
	public Properties getCriteria()
	{
		return criteriaList;
	}
	
	@Override
	public void setCriteria( Properties newCriteria )
	{
		this.criteriaList = newCriteria;
	}
	
	public List<MantisProcessorAction> getAfterProcessActions()
	{
		return this.afterProcessActions;
	}
	public void setAfterProcessActions( List<MantisProcessorAction> actions )
	{
		this.afterProcessActions = actions;
	}
	
	public void setAttachOriginalMessage( boolean newAttachOriginal )
	{
		this.attachOriginalMessage = newAttachOriginal;
	}
	
	@Override
	public boolean matchCriteria( MailMessage msg )
	{
		// decides whether the message should be processed by 
		// this processor or not
		return true;
	}
	
	/**
	 * [ns##issueid] -> \[ns(\d+)\] (a proper regular expression)
	 * @return
	 */
	private String convertStampToRegexp( )
	{
		String expression;
		
		expression = issueStamp.replace("##issueid", "(\\d+)");
		expression = expression.replace( "[", "\\[" );
		expression = expression.replace( "]", "\\]" );
		
		return expression;
	}
	
	private String getIssueNumberFromSubject( MailMessage msg )
	{
		String identifiedIssue = "";
		String onlyIssueId = "";
		
		// guess (by the subject) if the 
		// message corresponds to an existing 
		// issue or to a completely new one
		// from a generic issue "[au##issueid]" to regexp: "[au(\d+)]" 
		Pattern issueRegexp = Pattern.compile( convertStampToRegexp() );
		
		Matcher toFind = issueRegexp.matcher( msg.getSubject() );
		
		if( toFind.find() )
		{
			identifiedIssue = toFind.group();
			
			Pattern extractNumeric = Pattern.compile( "\\d+" );
			Matcher justNumber = extractNumeric.matcher( identifiedIssue );
			
			if( justNumber.find() )
			{
				onlyIssueId = justNumber.group();
			} // justNumber...
			
		} // toFind.find()

		
		return onlyIssueId;
	}
	
	@Override
	public void process(MailMessage msg)
	{
		boolean issueExist = false; 
		Long longIdentifiedIssue = 0L;
		
		try
		{			
			// try to identify a mantis issue number 
			String identifiedIssue = getIssueNumberFromSubject( msg );
			
			if( identifiedIssue != null && !identifiedIssue.isEmpty() )
			{
				longIdentifiedIssue =  Long.parseLong( identifiedIssue );
			
				issueExist =  mantis.issueNumberExists( longIdentifiedIssue );
			} //identifiedIssue != null 
			
			// try to find this issue number in mantis
			if( issueExist )
			{
				// an existing issue, just add a note
				longIdentifiedIssue = processExisting( msg, longIdentifiedIssue );
			}
			else
			{
				// a new issue
				longIdentifiedIssue = processNew( msg );
			}
			
			// perform additional actions that are in the actions list
			if( longIdentifiedIssue > 0 )
			{
				Iterator<MantisProcessorAction> itAction = afterProcessActions.iterator();
				while( itAction.hasNext() )
				{
					MantisProcessorAction action = itAction.next();
					action.doAction( longIdentifiedIssue, msg, issueExist );
				} // itAction.hasNext()			
			}
			
		}catch( JNException e )
		{
			// in case of exception, do nothing 
			// but raise the error
			log.error( e ); 
		}
		
	} // process
	
	
	private void saveAttachemnts( MailMessage msg, long issueNumber )
	{
		try
		{
			//
			// process attachments
			//
			// for all chunks in the message....
			Iterator<MailChunk> itChunk = msg.getAllChunks().iterator();
			while( itChunk.hasNext() )
			{
				MailChunk ch = itChunk.next();
				if( ch.isAttachment() )
				{
					IssueDataAttachment issueDataAttach = mantis.newIssueDataAttachment();
					
					issueDataAttach.setContent( ch.getAttachment() );
					issueDataAttach.setContentType( ch.getAttachmentContentType() );
					issueDataAttach.setFilename( ch.getAttachmentFilename() );
					
					mantis.addIssueAttachment( issueNumber, issueDataAttach );
				} // ch.isAttachment
				
			} // itChunk.hasNext()
			
		}catch( JNException e )
		{
			// log error and abort operation
			log.error( e ); 
		}

	}
	
	// add the original message as an attachment with 
	// the subject as the name of the filename
	private void doAttachOriginalMessage( MailMessage msg, long issueNumber, long noteNumber )
	{
		try
		{
			StringBuilder originalMessage = new StringBuilder();
			
			// get all the text parts of the message
			List <MailChunk> allChunks = msg.getAllChunks();
			Iterator<MailChunk> itChunk = allChunks.iterator();
			while( itChunk.hasNext() )
			{
				MailChunk ch = itChunk.next();
				if( ch.isHtml() || ch.isText() )
				{
					String textMsg = ch.getAsSimpleHtml();
					if( textMsg != null )
						originalMessage.append( textMsg );					
				} // ch.isHtml() || ch.isText()
			} // itChunk.hasNext()
			
			IssueDataAttachment issueDataAttach = mantis.newIssueDataAttachment();
			
			issueDataAttach.setContent( originalMessage.toString().getBytes() );
			issueDataAttach.setContentType( "text/html" );
			
			String filename; 
			filename = "##number - ##subject";
			filename = filename.replace( "##subject", msg.getSubject() );
			if( noteNumber != 0 )
				filename = filename.replace( "##number", Long.toString( noteNumber ) );
			else
				filename = filename.replace( "##number", Long.toString( issueNumber ) );
			
			issueDataAttach.setFilename( filename );
			
			mantis.addIssueAttachment( issueNumber, issueDataAttach );			
		}
		catch( JNException e )
		{
			// log error and abort operation
			log.error( e ); 
		}

		
	}
	
	public long processExisting( MailMessage msg, long issueNumber )
	{
		try
		{
			long noteNumber;
			
			String issueHeader = existingIssueHeaderTemplate; 

			issueHeader = issueHeader.replace( "##from", msg.getFromWithName() );
			issueHeader = issueHeader.replace( "##to", msg.getToWithName() );
			issueHeader = issueHeader.replace( "##sent", msg.getSentDate() );
			
			NoteData newNote = mantis.newNote();
			
			newNote.setPriv( false );
			newNote.setText( issueHeader + "\r\n\r\n" + msg.getFirstTextChunk() );
			
			noteNumber = mantis.addNote( issueNumber, newNote );
			
			if( this.attachOriginalMessage )
				doAttachOriginalMessage( msg, issueNumber, noteNumber );
			
			saveAttachemnts( msg, issueNumber );
			
			log.info( "Message changes saved" );
			
		}
		catch( Exception excep)
		{
			log.error("There was a problem modifying the message");
			log.error("Message id: " + msg.getId());
			log.error("Message subject: " + msg.getSubject() );
			log.error(excep);
		} // try Message
		
		return issueNumber;
		
	} // processExisting


	public long processNew(MailMessage msg) throws JNException
	{
		long issueId = 0L;
		
		try
		{
			String issueHeader = newIssueHeaderTemplate;
			
			issueHeader = issueHeader.replace( "##from", msg.getFromWithName() );
			issueHeader = issueHeader.replace( "##to", msg.getToWithName() );
			issueHeader = issueHeader.replace( "##sent", msg.getSentDate() );
			
			IssueDataDto newIssue = mantis.newIssue();

			newIssue.setSummary( msg.getSubject() );
			newIssue.setDescription( issueHeader + "\r\n\r\n" + msg.getBodyAsText() );
			//newIssue.setCategory( "General" );

			mantis.addIssue( newIssue );

			issueId = newIssue.getId();
			
			if( this.attachOriginalMessage )
				doAttachOriginalMessage( msg, newIssue.getId(), 0L );
			
			saveAttachemnts( msg, newIssue.getId() );
			
			log.info( "Message changes saved" );
			
		}catch( Exception excep)
		{
			log.error("There was a problem modifying the message");
			log.error("Message subject: " + msg.getSubject() );
			log.error(excep);
			throw new JNException( excep.getMessage() );
		} // try Message	

		return issueId;
	} // process


}
