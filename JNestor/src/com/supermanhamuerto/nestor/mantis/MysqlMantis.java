package com.supermanhamuerto.nestor.mantis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.hibernate.SessionFactory;
//import org.mantisbt.connect.IMCSession;
//import org.mantisbt.connect.MCException;
//import org.mantisbt.connect.axis.MCSession;
//import org.mantisbt.connect.model.CustomFieldValue;
//import org.mantisbt.connect.model.ICustomFieldValue;
//import org.mantisbt.connect.model.IIssue;
//import org.mantisbt.connect.model.IMCAttribute;
//import org.mantisbt.connect.model.INote;
//import org.mantisbt.connect.model.MCAttribute;
import org.springframework.jdbc.core.JdbcTemplate;

import com.supermanhamuerto.exception.JNException;
import com.supermanhamuerto.nestor.mantis.IssueDataDto;
import com.supermanhamuerto.nestor.mantis.IssueDataAttachment;
import com.supermanhamuerto.nestor.mantis.IssueDataFactory;
import com.supermanhamuerto.nestor.mantis.NoteData;

@Deprecated
public class MysqlMantis
{
	private static Log log = LogFactory.getLog(Mantis.class);

	private IssueDataFactory idf;
	private JdbcTemplate jdbc;
	
	private String user;
	private String password;
	private int projectId;
	private String category;

	public void setJdbcTemplate( JdbcTemplate jdbc )
	{
		this.jdbc = jdbc;
	}
	
	public void init() throws JNException
	{
	}
	
	public void openSession() throws JNException
	{
	}
	
	private int getUserId()
	{
		StringBuilder sql = new StringBuilder();
		
		sql.append( "select id ");
		sql.append( "from mantis_user_table ");
		sql.append( "where username = ? " );
		
		return jdbc.queryForInt(sql.toString(), user );
	}
	
	private int getCategoryId()
	{
		StringBuilder sql = new StringBuilder();
		
		sql.append( "select id " );
		sql.append( "from mantis_category_table " );
		sql.append( "where name = ? " );
		
		return jdbc.queryForInt( sql.toString(), category );
	}
	
	public boolean issueNumberExists( long issueid ) throws JNException
	{
		StringBuilder sql = new StringBuilder();
		List<Object> param = new ArrayList<Object>();
		
		sql.append( "select id " );
		sql.append( "from mantis_bug_table " );
		sql.append( "where id = ? " );
		param.add( issueid );
		
		int found = jdbc.queryForInt( sql.toString(), param.toArray() );
		
		return (long) found == issueid;
	} // issueNumberExists
	
	public IssueDataAttachment newIssueDataAttachment()
	{
		return idf.getIssueDataAttachment();
	}
	
	public long addIssueAttachment( long issueId, IssueDataAttachment attachment ) throws JNException
	{
		Date now = new Date();
		
		StringBuilder sql = new StringBuilder();
		List<Object> param = new ArrayList<Object>();
		
		sql.append( "insert into mantis_bug_file_table " );
		sql.append( "(bug_id, " );
		sql.append( "filename, " );
		sql.append( "filesize, " );
		sql.append( "file_type, " );
		sql.append( "content, " );
		sql.append( "date_added, " );
		sql.append( "user_id )" );
		sql.append( "values " );
		sql.append( "(?, " );
		param.add( issueId );
		sql.append( "?, " );
		param.add( attachment.getFilename() );
		sql.append( "?, " );
		param.add( attachment.getContent().length );
		sql.append( "?, " );
		param.add( attachment.getContentType() );
		sql.append( "?, " );
		param.add( attachment.getContent() );
		sql.append( "?, " );
		param.add( (int) now.getTime() );
		sql.append( "?) " );
		param.add( getUserId() );

		jdbc.update( sql.toString(), param.toArray() );
		return jdbc.queryForLong( "select last_insert_id()" );
	}
	
	public IssueDataDto newIssue()
	{
		return idf.getIssueData(); 
	}
	
	public void addIssue( IssueDataDto  issue ) throws JNException
	{
		long idText;
		Date now = new Date();
		
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		
		sql.append( "insert into mantis_bug_table ");
		sql.append( "( project_id, " );
		sql.append( " reporter_id, " );
		sql.append( " summary, " );
		sql.append( " category_id, " );
		sql.append( " date_submitted, " );
		sql.append( " last_updated ) " );
		sql.append( "values " );
		sql.append( "(?, " ); // project_id
		params.add( projectId );
		sql.append( "?, " ); // reporter_id
		params.add( getUserId() );
		sql.append( "?, " ); // summary
		params.add( issue.getSummary() );
		sql.append( "?, " ); // category_id
		params.add( getCategoryId() ); 
		sql.append( "?, " ); // date_submitted
		params.add( (int) now.getTime() );
		sql.append( "?) " ); // last_updated
		params.add( (int) now.getTime() );

		jdbc.update( sql.toString(), params.toArray() );
		
		issue.setId( jdbc.queryForLong( "select last_insert_id()" ) );		
		log.info( "Created new mantis issue with number " + issue.getId() );
		
		// step 2: add texts to the issue in mantis_bug_text_table
		StringBuilder sql2 = new StringBuilder();
		List<Object> params2 = new ArrayList<Object>();
		
		sql2.append( "insert into mantis_bug_text_table " );
		sql2.append( "( description, " );
		sql2.append( "steps_to_reproduce, " );
		sql2.append( "additional_information )" );
		sql2.append( "values " );
		sql2.append( "( ?, " );
		params2.add( issue.getDescription() );
		sql2.append( "?, " );
		params2.add( "" );
		sql2.append( "? ) " );
		params2.add( "" );

		jdbc.update( sql2.toString(), params2.toArray() ); 
		
		idText = jdbc.queryForLong( "select last_insert_id()" );
		
		// step 3: link the added texts into the issue
		StringBuilder sql3 = new StringBuilder();
		List<Object> params3 = new ArrayList<Object>();
		
		sql3.append( "update mantis_bug_table set bug_text_id = ? where id = ? " );
		params3.add( idText );
		params3.add( issue.getId() ); 
		
		jdbc.update( sql3.toString(), params3.toArray() );


	} // addIssue
	
	public NoteData newNote()
	{
		return idf.getNoteData();
	}
	
	public long addNote( long issue, NoteData noteData ) throws JNException
	{
		Date now = new Date();
		
		// step 1: add note to mantis_bugnote_table
		StringBuilder sql = new StringBuilder(); 
		List<Object> params = new ArrayList<Object>();
		
		sql.append( "insert into mantis_bugnote_table " );
		sql.append( "( bug_id, " );
		sql.append( " reporter_id, " );
		sql.append( " last_modified, " );
		sql.append( " date_submitted ) " );
		sql.append( " values " );
		sql.append( " (?, " ); // bug_id
		params.add( issue ); 
		sql.append( " ?, " ); // reporter_id
		params.add( getUserId() );
		sql.append( " ?, " ); // last_modified
		params.add( (int) now.getTime() );
		sql.append( " ?) " ); // date_submitted
		params.add( (int) now.getTime() );
		
		jdbc.update( sql.toString(), params.toArray() );

		// set the id of the new note
		noteData.setId( jdbc.queryForLong( "select last_insert_id()" ) );
		
		// step 2: add text to the note 
		// in the table mantis_bugnote_text_table
		StringBuilder sql2 = new StringBuilder();
		List<Object> params2 = new ArrayList<Object>();
		
		sql2.append( "insert into mantis_bugnote_text_table " );
		sql2.append( "(note)" );
		sql2.append( "values" );
		sql2.append( "( ? ) " );
		params2.add( noteData.getText() );

		jdbc.update( sql2.toString(), params2.toArray() );
		long noteTextId = jdbc.queryForLong( "select last_insert_id()" );
		
		// step 3: link the two tables
		StringBuilder sql3 = new StringBuilder();
		List<Object> params3 = new ArrayList<Object>();
		
		sql3.append( "update mantis_bugnote_table set bugnote_text_id = ? " );
		params3.add( noteTextId );
		sql3.append( " where id = ? " );
		params3.add( noteData.getId() ); 
		
		jdbc.update( sql3.toString(), params3.toArray() );
		
		return noteData.getId();
		
	} // addNote
	
	
	public void addCustomField( long issue, String field, String value ) throws JNException
	{
		
		// step 1: get the field id from the table mantis_custom_field_value 
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		
		//sql.append( "select id, name, type, possible_values  " );
		sql.append( "select id  " );
		sql.append( " from mantis_custom_field_table " );
		sql.append( " where name = ? " );
		params.add( field );
		
		long fieldId = jdbc.queryForLong( sql.toString(), params.toArray() );
		
		if( fieldId != 0L )
		{
			// step 2: check if exists a previous value and has to be updated
			StringBuilder sql2 = new StringBuilder();
			List<Object> params2 = new ArrayList<Object>();
			
			sql2.append( "select count(*) " );
			sql2.append( " from mantis_custom_field_string_table " );
			sql2.append( "where bug_id = ? " );
			params2.add( issue );
			sql2.append( "  and field_id = ? " );
			params2.add( fieldId );
			
			int customFieldExist = jdbc.queryForInt( sql2.toString(), params2.toArray() );
			
			if( customFieldExist == 0 )
			{
				// step 3: if it doesn't exist, add it
				StringBuilder sql3 = new StringBuilder();
				List<Object> params3 = new ArrayList<Object>();
				
				sql3.append( "insert into mantis_custom_field_string_table " );
				sql3.append( "( field_id, " );
				sql3.append( "bug_id, " );
				sql3.append( "value ) " );
				sql3.append( "values " );
				sql3.append( "(?, " );
				params3.add( fieldId );
				sql3.append( "?, " );
				params3.add( issue );
				sql3.append( "?) " );
				params3.add( value ); 

				jdbc.update( sql3.toString(), params3.toArray() );
			}
			else
			{
				// step 4: custom field exist, change its value
				StringBuilder sql4 = new StringBuilder();
				List<Object> params4 = new ArrayList<Object>();
				
				sql4.append( "update mantis_custom_field_string_table " );
				sql4.append( "set value = ? " );
				params4.add( value );
				sql4.append( "where bug_id = ? " );
				params4.add( issue );
				sql4.append( " and field_id = ? " );
				params4.add( fieldId );
				
				jdbc.update( sql4.toString(), params4.toArray() );
				
			} //  customFieldExist == 0
			
			
			
		} // fieldId != 0L
		
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
	
	public long addRelationship( long origIssue, long destIssue, RelationshipType relation) throws JNException
	{
		if( origIssue == destIssue )
		{
			log.error( "Origin and Destination issues to be related are the same: " + origIssue  );
			throw new JNException( "Origin and Destination issues to be related are the same: " + origIssue );
		}
		
		// step 0: check that there isn't a previous relationship among
		// the two issues
		StringBuilder sql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		
		sql.append( "select count(*) ");
		sql.append( "from mantis_bug_relationship_table ");
		sql.append( "where " );
		sql.append( "(source_bug_id = ? and destination_bug_id = ? )");
		params.add( origIssue );
		params.add( destIssue ); 
		sql.append( " or " );
		sql.append( "(source_bug_id = ? and destination_bug_id = ? ) ");
		params.add( destIssue ); 
		params.add( origIssue );
		
		int exist = jdbc.queryForInt( sql.toString(), params.toArray() );
		
		if( exist > 0 )
		{
			log.error( "There exist a previous relationship among bugs " + origIssue + " and " + destIssue );
			throw new JNException("There exist a previous relationship among bugs " + origIssue + " and " + destIssue );
		}
		
		assert origIssue != destIssue && exist == 0; 
		
		StringBuilder sql2 = new StringBuilder();
		List<Object> params2 = new ArrayList<Object>();
		
		sql2.append( "insert into mantis_bug_relationship_table " );
		sql2.append( "( source_bug_id, " );
		sql2.append( "destination_bug_id, " );
		sql2.append( "relationship_type ) " );
		sql2.append( "values " );
		sql2.append( "(?, " );
		params2.add( origIssue );
		sql2.append( "?, " );
		params2.add( destIssue );
		sql2.append( "?) " );
		params2.add( relation.getValue() );
		
		jdbc.update( sql2.toString(), params2.toArray() );
		
		return jdbc.queryForInt("select last_insert_id()");
		
	} // addRelationship
	
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

	public int getProjectId()
    {
    	return projectId;
    }

	public void setProjectId(int projectId)
    {
    	this.projectId = projectId;
    }

	public String getCategory()
    {
	    return category;
    }
	public void setCategory(String category)
    {
	    this.category = category;
    }

	public IssueDataFactory getIdf()
    {
    	return idf;
    }

	public void setIdf(IssueDataFactory idf)
    {
    	this.idf = idf;
    }
	
	
}
