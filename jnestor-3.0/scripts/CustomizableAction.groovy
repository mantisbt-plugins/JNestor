//
// CustomField.groovy
//
// 
package scripts;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.supermanhamuerto.nestor.MailMessage;
import com.supermanhamuerto.nestor.mantis.Mantis;
import com.supermanhamuerto.nestor.mantis.MantisProcessorAction;
import com.supermanhamuerto.exception.JNException;

// define the implementation 
class CustomizableAction implements MantisProcessorAction
{
	private static Log log = LogFactory.getLog("CustomizableAction.groovy");

	Mantis mantis; 
	
	public void setMantis( Mantis mantis ) 
	{
		this.mantis = mantis; 
	}

	
	public void doAction( long issueId, MailMessage msg, boolean exists ) throws JNException
	{
		// depending of the "from" address, add a custom field or other
		String fromAddress = msg.getFrom();
		
		log.info( "CustomizableAction: address " + fromAddress );
		log.info( "CustomizableAction: ends with .com? " + fromAddress.endsWith( ".com" ) );
		
		if( fromAddress.endsWith( ".com" ) )
		{
			mantis.addCustomField( issueId, "Country", "spain" );
			log.info( "CustomizableAction: added country spain" );
		} // if

	} // doAction 
	 
}

