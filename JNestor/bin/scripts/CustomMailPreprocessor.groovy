//
// CustomMailPreprocessor.groovy
//
// 
// This script is codified as UTF-8 without BOM. For editing purposes. 
// 

package scripts;

import com.supermanhamuerto.exception.JNException
import com.supermanhamuerto.nestor.MailMessage
import com.supermanhamuerto.nestor.MailPreprocessor

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;


// define the implementation 
class CustomMailPreprocessor implements MailPreprocessor
{
 	def nestorSubject;
	
	public MailMessage applyCustomRules( MailMessage mailMsg )
	{
		boolean out;
		String subject = mailMsg.getSubject();
		
		out = true;
		
		out = out && !subject.matches( "^" + nestorSubject );
		
		// if you want to create a custom filter to process 
		// only certain messages, uncomment this and customize it
		// out = out && subject.matches( regex ); 
		
		// spanish
		out = out && !subject.matches( "^entregado\\:.*" );
		out = out && !subject.matches( "^leer\\:.*" );
		out = out && !subject.matches( "^le.do\\:.*" );
		out = out && !subject.matches( "^no le.do\\:.*" );
		out = out && !subject.matches( "^respuesta autom.tica de fuera de la oficina\\:.*" );
		out = out && !subject.matches( "^respuesta de estoy ausente\\:.*" );
        // portuguese, brazilian portuguese
		out = out && !subject.matches( "^lida\\: .*" );
		out = out && !subject.matches( "^resposta autom.*tica de ausencia tempor.*ria.*" );
		out = out && !subject.matches( "^Lido\\:.*" );
		out = out && !subject.matches( "^Letto\\:.*" );
		out = out && !subject.matches( "^Não lido\\:.*" );
        // english
		out = out && !subject.matches( "^read\\: .*" );
		out = out && !subject.matches( "^automatic reply\\:.*" );
		out = out && !subject.matches( "^auto\\:.*is out of the office.*" );
		out = out && !subject.matches( "^auto\:.*is traveling to.*" );
		out = out && !subject.matches( "^out of office autoreply\\: .*" );
        // turkish
		out = out && !subject.matches( "^okundu\\:.*" );
		out = out && !subject.matches( "^teslim edildi\\:.*" );
        // mail server specific
		out = out && !subject.matches( "^delayed mail (still being retried)" );
		out = out && !subject.matches( "^undelivered mail returned to sender.*" );
        // business specific
		out = out && !subject.matches( ".*pruebas raul.*" );
		out = out && !subject.matches( "^auditmap\\: alarm.*" );
		out = out && !subject.matches( "^auditmap\\: alerta.*" );
		out = out && !subject.matches( "^Undeliverable\\:.*" );

		if( out )
		{
			// get the body of the message and cut some
			// text
			String body = mailMsg.getFirstHtmlChunk();

			mailMsg.setTextBody( smartCut( body, 
											1000, 
											7, 
											"____", 
											"Regards,",
											"Saludos",
											"Un cordial saludo" ) ); 

			return mailMsg;
		}
		else
			return null;

	} // applyCustomRules



 /**
   * <p>
   * Performs a "smartCut" of the body. Bodies nowadays
   * have long disclaimer texts at the end, or could
   * carry all the previous history of the email
   * (tons of answers, re-answers, etc.): this is
   * highly distracting and should be removed in
   * favor of keep more clean the message.</p>
   * <p>
   * The rules implemented here are as follows:</p>
   * <ol>
   * <li>Filter the html to basic html
   * <li>Cut at "firstCut" chars (default:1000): all
   *     the remaining characters will be discarded,
   *     and possibly attached to the issue as an
   *     attachment.
   * <li>Count the lines of the text: at "maxLinesSecondCut"
   *     lines, stop
   * <li>If before reaching those "maxLinesSecondCut" we
   *     find one goodbye formula, stop at this line. These
   *     goodByeFormulas are regular expressions stored
   *     in the list "goodByeFormulas"
   * </ol>
   */
private String smartCut( String input,
           int firstCut = 1000,
           int maxLinesSecondCut = 7,
           String[] greetings = ["_____"] )
{
    String out;
 
    out = Jsoup.clean( input, Whitelist.basic() );
   
	// TODO: count blank lines and remove 
	// the empty blank lines
   
    if( out.length() > firstCut )
     out = out.substring( 0, firstCut );
 
	// iterate through the remaining characters
	boolean insideTag = false;
	int linesFound = 0;
	int pos = 0;
	int lastCutPosition = 0;
	boolean stopHere = false;
	while( pos < out.length() - 10 && linesFound < maxLinesSecondCut && !stopHere )
	{
		String test = out.substring( pos );
	  
		if( test.startsWith( "<br/>" )
		||  test.startsWith( "<br />" )
		||  test.startsWith( "<p/>" )
		||  test.startsWith( "<p />" ) )
		{
			linesFound++;
		} // if test.startsWith
	 
		if( test.startsWith( "<" ) )
		{
			lastCutPosition = pos;
			insideTag = true;
		}
		if( test.startsWith(  ">" ) )
		{
			insideTag = false;
		}
	  
		if( greetings )
		{
			Iterator<String> itGreetings = greetings.iterator();
			while( itGreetings.hasNext() )
			{
				String greetingFormula = itGreetings.next();
				 
				if( test.startsWith( greetingFormula ) )
				{
					stopHere = true;
				} // if test.contains()
			    
			} // while	 
		} // if greetings
		 
		pos++;
	    
	} // while
   
    out = out.substring( 0, lastCutPosition );
   
    return out;
  }



} // CustomMailPreprocessor


