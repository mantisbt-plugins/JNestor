//
// CustomizableProcess.groovy
//
// This script is codified as UTF-8 without BOM. For editing purposes. 
// 

package scripts;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.supermanhamuerto.nestor.MailMessage;
import com.supermanhamuerto.nestor.MailProperty;
import com.supermanhamuerto.nestor.MailProcessor;
import com.supermanhamuerto.nestor.mantis.Mantis;

// define the implementation 
class CustomizableProcess implements MailProcessor
{
	private static Log log = LogFactory.getLog("CustomizableAction.groovy");

	Mantis mantis; 
	
	public void setMantis( Mantis mantis ) 
	{
		this.mantis = mantis; 
	}

	
	public void process( MailMessage msg )
	{
		// depending of the "from" address, add a custom field or other
		String fromAddress = msg.getFrom();
		Long issueId = (Long) msg.getProperty( MailProperty.ISSUE_ID );
		
		log.info( "CustomizableAction: address " + fromAddress );
		log.info( "CustomizableAction: ends with .com? " + fromAddress.endsWith( ".com" ) );
		
		if( fromAddress.endsWith( ".com" ) )
		{
			mantis.addCustomField( issueId, "Country", "spain" );
			log.info( "CustomizableAction: added country spain" );
		} // if

	} // doAction 
	 
}

