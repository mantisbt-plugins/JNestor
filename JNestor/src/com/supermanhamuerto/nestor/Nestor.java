package com.supermanhamuerto.nestor;

import org.apache.commons.logging.Log;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.supermanhamuerto.exception.JNException;
import com.supermanhamuerto.nestor.mantis.Mantis;

public class Nestor 
{
	public static final String PROGNAME = "Jnestor";
	public static final String PROGVERSION = "3.0 Beta";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		Log log = (Log) ctx.getBean("logger");
		
		try
		{
			System.out.println( PROGNAME + " " + PROGVERSION );
			
//			MantisProcessorAction cb = (MantisProcessorAction) ctx.getBean("groovyAction");
			
//			cb.doAction(0, null, false);
			
			// TODO: perform initial checking of all the 
			// functionalities to avoid some wrongdoings 
			// end with fill up mantis with false issues
			// Things to checks: 
			// 1) that local folder is properly working
			// 2) that the message id of a message can be obtained
			// 3) that mantis can be accessed
			
			Mantis mantis = (Mantis) ctx.getBean("mantis");
			
			mantis.openSession();

			// some tests of the mantis 
			//mantis.issueNumberExists(84);

			//NoteData note = mantis.newNote();
			
			//note.setPriv( false );
			//note.setText("hello from addNote" );
			//mantis.addNote(84, note);
			
			MailReader mailReader = (MailReader) ctx.getBean("mailReader");
			
			log.info("Starting to read emails....");
			
			mailReader.readEmails();
			
		}catch( JNException e )
		{
			log.error( e );
		}
		
		log.info("Finished");

	}

}
