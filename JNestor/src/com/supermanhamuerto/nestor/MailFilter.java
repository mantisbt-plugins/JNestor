package com.supermanhamuerto.nestor;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Deprecated
public class MailFilter
{
	private static Log log = LogFactory.getLog(MailFilter.class);

	private Properties subjectFilter; 
	private Properties bodyFilter;
	
	public Properties getSubjectFilter()
    {
    	return subjectFilter;
    }
	public void setSubjectFilter(Properties subjectFilter)
    {
    	this.subjectFilter = subjectFilter;
    }
	public Properties getBodyFilter()
    {
    	return bodyFilter;
    }
	public void setBodyFilter(Properties bodyFilter)
    {
    	this.bodyFilter = bodyFilter;
    }
	
	public boolean isOk( String subject )
	{
		// apply all the filters of subject
		String subRegexp;
		String subMatching;
		Pattern pattern;
		Matcher matcher;

		
		log.debug( "Testing if subject " + subject + " should be processed" );
		//
		// contains subject patterns
		//
		boolean matchesContains = false;
		int idFilter = 1;
		do
		{
			
			subRegexp = subjectFilter.getProperty( "exp" +  idFilter,  "###empty###");
			subMatching = subjectFilter.getProperty( "matching" + idFilter, "contains" );
			if( !subRegexp.equals("###empty###") && subMatching.equals( "contains" ) )
			{
				pattern = Pattern.compile( subRegexp, Pattern.CASE_INSENSITIVE );
				matcher = pattern.matcher( subject );
				// contains
				matchesContains = matchesContains || matcher.find();					
				log.debug( "Regular expression: " + subRegexp );
				log.debug( "Contains or not-contains? " + subMatching );
				log.debug( "Matches? " + matchesContains );
			}
			idFilter++;
		}while( !matchesContains && !subRegexp.equals( "###empty###") );


		//
		// not-contains subject patterns
		//
		idFilter = 1;
		boolean matchesNotContains = false;		
		do
		{
			
			subRegexp = subjectFilter.getProperty( "exp" +  idFilter,  "###empty###");
			subMatching = subjectFilter.getProperty( "matching" + idFilter, "contains" );
			if( !subRegexp.equals("###empty###") && subMatching.equals( "not-contains" ) )
			{
				pattern = Pattern.compile( subRegexp, Pattern.CASE_INSENSITIVE );
				matcher = pattern.matcher( subject );
				// not-contains
				matchesNotContains = matchesNotContains || matcher.find();
				log.debug( "Regular expression: " + subRegexp );
				log.debug( "Contains or not-contains? " + subMatching );
				log.debug( "Matches? " + matchesNotContains );
			}
			idFilter++;
		}while( !matchesNotContains && !subRegexp.equals( "###empty###") );
		
		
		return !matchesNotContains && matchesContains;
	}
	
}
