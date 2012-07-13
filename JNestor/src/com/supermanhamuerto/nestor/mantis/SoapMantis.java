package com.supermanhamuerto.nestor.mantis;

import java.math.BigInteger;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.supermanhamuerto.exception.JNException;
import com.supermanhamuerto.nestor.mantis.IssueDataDto;
import com.supermanhamuerto.nestor.mantis.IssueDataAttachment;
import com.supermanhamuerto.nestor.mantis.IssueDataFactory;
import com.supermanhamuerto.nestor.mantis.NoteData;

import biz.futureware.mantis.rpc.soap.client.CustomFieldDefinitionData;
import biz.futureware.mantis.rpc.soap.client.CustomFieldValueForIssueData;
import biz.futureware.mantis.rpc.soap.client.IssueData;
import biz.futureware.mantis.rpc.soap.client.IssueNoteData;
import biz.futureware.mantis.rpc.soap.client.MantisConnectLocator;
import biz.futureware.mantis.rpc.soap.client.MantisConnectPortType;
import biz.futureware.mantis.rpc.soap.client.ObjectRef;
import biz.futureware.mantis.rpc.soap.client.RelationshipData;


public class SoapMantis implements Mantis
{
	private static Log log = LogFactory.getLog(SoapMantis.class);

	private IssueDataFactory idf;

	private MantisConnectPortType portType;
	
	private String user;
	private String password;
	private String url;

	private int projectId;
	private String category;

	
	public String getUser()
	{
		return user;
	}
	public void setUser(String user)
  {
		this.user = user;
  }
	public String getPassword()
  {
  	return password;
  }
	public void setPassword(String password)
  {
  	this.password = password;
  }

	public String getUrl()
	{
		return url;
	}
	public void setUrl( String url )
	{
		this.url = url;
	}
	
	public int getProjectId()
  {
  	return projectId;
  }

	public void setProjectId(int projectId)
  {
  	this.projectId = projectId;
  }

	/* (non-Javadoc)
   * @see com.supermanhamuerto.nestor.mantis.MantisInterface#getCategory()
   */
	@Override
  public String getCategory()
  {
    return category;
  }
	/* (non-Javadoc)
   * @see com.supermanhamuerto.nestor.mantis.MantisInterface#setCategory(java.lang.String)
   */
	@Override
  public void setCategory(String category)
  {
    this.category = category;
  }

	/* (non-Javadoc)
   * @see com.supermanhamuerto.nestor.mantis.MantisInterface#getIdf()
   */
	@Override
  public IssueDataFactory getIdf()
  {
  	return idf;
  }

	/* (non-Javadoc)
   * @see com.supermanhamuerto.nestor.mantis.MantisInterface#setIdf(com.supermanhamuerto.nestor.mantis.IssueDataFactory)
   */
	@Override
  public void setIdf(IssueDataFactory idf)
  {
  	this.idf = idf;
  }

	/* (non-Javadoc)
   * @see com.supermanhamuerto.nestor.mantis.MantisInterface#init()
   */
	@Override
  public void init() throws JNException
	{
	}
	
	/* (non-Javadoc)
   * @see com.supermanhamuerto.nestor.mantis.MantisInterface#openSession()
   */
	@Override
  public void openSession() throws JNException
	{
		try
		{
			URL url = new URL(this.url);
			
			MantisConnectLocator mcl = new MantisConnectLocator();
			portType = mcl.getMantisConnectPort( url ); 
		
		}catch( Exception e )
		{
			log.error( e );
			throw new JNException( e );
		}

	}
	
	/* (non-Javadoc)
   * @see com.supermanhamuerto.nestor.mantis.MantisInterface#issueNumberExists(long)
   */
	@Override
  public boolean issueNumberExists( long issueid ) throws JNException
	{
		try
		{
			return portType.mc_issue_exists(user, password, BigInteger.valueOf( issueid ) );
		}catch( Exception e )
		{
			log.error( e );
			throw new JNException( e );
		}
	} // issueNumberExists
	
	/* (non-Javadoc)
   * @see com.supermanhamuerto.nestor.mantis.MantisInterface#newIssueDataAttachment()
   */
	@Override
  public IssueDataAttachment newIssueDataAttachment()
	{
		return idf.getIssueDataAttachment();
	}
	
	/* (non-Javadoc)
   * @see com.supermanhamuerto.nestor.mantis.MantisInterface#addIssueAttachment(long, com.supermanhamuerto.nestor.mantis.IssueDataAttachment)
   */
	@Override
  public long addIssueAttachment( long issueId, IssueDataAttachment attachment ) throws JNException
	{
		BigInteger out;
		
		try
		{
			out = portType.mc_issue_attachment_add( user, password, 
                                    					BigInteger.valueOf(issueId), 
                                    					attachment.getFilename(), 
                                    					attachment.getContentType(), 
                                    					attachment.getContent() );
			return out.longValue();
		}catch( Exception e )
		{
			log.error( e );
			throw new JNException( e );
		} // try
	}
	
	/* (non-Javadoc)
   * @see com.supermanhamuerto.nestor.mantis.MantisInterface#newIssue()
   */
	@Override
  public IssueDataDto newIssue()
	{
		return idf.getIssueData(); 
	}
	
	/* (non-Javadoc)
   * @see com.supermanhamuerto.nestor.mantis.MantisInterface#addIssue(com.supermanhamuerto.nestor.mantis.IssueDataDto)
   */
	@Override
  public long addIssue( IssueDataDto  issue ) throws JNException
	{
		BigInteger createdIssue;
		IssueData issue2 = new IssueData();
		ObjectRef project = new ObjectRef();
		project.setId( BigInteger.valueOf(projectId) );
		
		try
		{
			issue2.setSummary( issue.getSummary() );
			issue2.setCategory( category );
			issue2.setDescription( issue.getDescription() );
			issue2.setProject( project );
			
			createdIssue = portType.mc_issue_add( user, password, issue2 );

			issue.setId( createdIssue.longValue() );
			
			return issue.getId();
		}catch( Exception e )
		{
			log.error( e );
			throw new JNException( e );
		}
		
	} // addIssue
	
	/* (non-Javadoc)
   * @see com.supermanhamuerto.nestor.mantis.MantisInterface#newNote()
   */
	@Override
  public NoteData newNote()
	{
		return idf.getNoteData();
	}
	
	/* (non-Javadoc)
   * @see com.supermanhamuerto.nestor.mantis.MantisInterface#addNote(long, com.supermanhamuerto.nestor.mantis.NoteData)
   */
	@Override
  public long addNote( long issue, NoteData noteData ) throws JNException
	{
		IssueNoteData noteData2 = new IssueNoteData();
		BigInteger noteDataId;
		
		try
		{
			noteData2.setText( noteData.getText() );

			noteDataId = portType.mc_issue_note_add( user, password, 
																							BigInteger.valueOf(issue), 
																							noteData2 );
			noteData.setId( noteDataId.longValue() );
			
		}catch( Exception e )
		{
			log.error( e );
			throw new JNException(e);
		}
		
		return noteData.getId();
		
	} // addNote
	
	
	/* (non-Javadoc)
   * @see com.supermanhamuerto.nestor.mantis.MantisInterface#addCustomField(long, java.lang.String, java.lang.String)
   */
	@Override
  public void addCustomField( long issue, String field, String value ) throws JNException
	{
		IssueData issue2;
		CustomFieldValueForIssueData cfv = new CustomFieldValueForIssueData();

		try
		{
			
			// STEP 1: read the issue data
			issue2 = portType.mc_issue_get(user, password, BigInteger.valueOf(issue));

			// STEP 2: identify the objectRef of the field "field"
			ObjectRef objectField = null;

			CustomFieldDefinitionData[] listOfFields = 
															portType.mc_project_get_custom_fields( user, password, 
																											BigInteger.valueOf(projectId) );
			

			for( CustomFieldDefinitionData aField : listOfFields )
			{
				if( aField.getField().getName().equals( field ) )
				{
					objectField = aField.getField();
				}
			} // for 
			
			// STEP 3: fill up a CustomFieldValueForIssueData 
			// with the information of the field and value
			cfv.setField( objectField );
			cfv.setValue( value );

			// STEP 4: feed the information of the custom field
			// to the issue and make an update of the information
			CustomFieldValueForIssueData[] cfvArray = { cfv };

			issue2.setCustom_fields( cfvArray );
			
			// STEP 5: guardamos los datos
			portType.mc_issue_update(user, password, BigInteger.valueOf(issue), issue2);

		}catch( Exception e )
		{
			log.error( e );
			throw new JNException(e);
		}
				
	}
	
	public static enum RelationshipType
	{
		DUPLICATE_OF(0), RELATED_TO(1), PARENT_OF(2), CHILD_OF(3), HAS_DUPLICATE(4);
		
		private int type; 
		
		private RelationshipType( int type )
		{
			this.type = type;
		}
		public int getValue()
		{
			return this.type;
		}
	}
	
	/* (non-Javadoc)
   * @see com.supermanhamuerto.nestor.mantis.MantisInterface#addRelationship(long, long, com.supermanhamuerto.nestor.mantis.SoapMantis.RelationshipType)
   */
	@Override
  public long addRelationship( long origIssue, long destIssue, RelationshipType relation) throws JNException
	{
		RelationshipData relData = new RelationshipData();
		ObjectRef objectRef = new ObjectRef();
		BigInteger out; 
		
		try
		{
			
			relData.setId( BigInteger.valueOf( origIssue ) );
			relData.setTarget_id( BigInteger.valueOf( destIssue ) );
			relData.setType( objectRef );
			
			out = portType.mc_issue_relationship_add( user, password, BigInteger.valueOf(origIssue), relData );
			
			return out.longValue();
			
		}catch( Exception e )
		{
			log.error( e );
			throw new JNException( e );
		}
		
	} // addRelationship
	
	
	
}
