<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
<meta name="description" content="description"/>
<meta name="keywords" content="keywords"/> 
<meta name="author" content="author"/> 
<link rel="stylesheet" type="text/css" href="css/default.css" media="screen"/>
<title>JNestor: another majordomo in java</title>
</head>

<body>

<div class="outer-container">

<div class="inner-container">

<?php
include 'header.php';
?>

	<div class="path">
			
			<a href="/">Home</a> &#8250; <a href="contact.php">Contact</a>

	</div>

	<div class="main">		
		
		<div class="content">

			<h1>Latest News</h1>
			
			<h2>Version 3.0 Beta released</h2>
            <div class="descr">Last update: May 27, 2012</div>

      <p>
      I am proud to announce that I have released the version 3.0 beta. 
      </p>

      <p>
      Since february much work have been done in order to produce an stable
      version of Jnestor, and a major refactoring of many parts of the project
      have been made.</p>

      <p>
      This version has major changes compared against previous releases:</p>

      <ul>
      <li>
      The mantis connection technology are now the <a href="http://search.maven.org/#search|gav|1|g%3A%22biz.futureware.mantis%22%20AND%20a%3A%22mantis-axis-soap-client%22">stub libraries for SOAP</a> 
      instead MantisConnect. Later I've discovered that MantisConnect weren't 
      supported and I started a process to find a new technology to replace it.
      Thanks to Robert Munteanu that put me in the lead to the good solution. </li>
      <li>
      I've installed a "local folder" object that stores the message id in a 
      database, and checks if the message is stored to avoid treat it again. 
      This prevents the flooding of false issues when the message is not 
      deleted from the inbox folder. I've installed a version without this 
      feature and it created 5,000 (yes five thousand) messages in one night
      of functioning. With this feature, who can't be removed by error, it 
      is impossible to treat the same message two times.</li>
      <li>
      Instead of the filter by subject and the complex XML configuration in 
      place, I've created an object called "CustomMailPreprocessor" who is 
      written in <a href="http://groovy.codehaus.org/">Groovy</a>. This is 
      fairly easy to modify and customize what messages should be treated and 
      what messages don't.</li>
      <li>
      One thing that I've discovered while running the previous versions of 
      Jnestor was that the "disclaimer" section of the emails fills the 
      issues with garbage. In order to prevent this, I've cut the messages
      to the seventh line and attach the original message as a web page. 
      This can be found and modified at your convenience in the "CustomMailPreprocessor"
      groovy object. Don't be afraid of changing it to fit your needs.
      The result is that only the revelant information is there and -at least 
      in our cases- we didn't need to check the original page or the original 
      message to see parts of the message that are deleted by error. And 
      we got rid of these disclaimer paragraphs.</li>
      <li>
			Another interesting thing is that there is an example of custom 
      actions that you can do whenever a issue is entered in the database. 
      I've created a CustomizableAction in groovy that is configured to 
      fill in a "Country" field, but imagination is free to add your own 
      stuff here. <a href="http://lists.sourceforge.net/lists/listinfo/jnestor-general">Share your ideas with the list</a> and 
      I will give you the hints you may need!!!</li>
      <li>
      A big problem I had at the first stages of the project was the resending 
      of the email back with the issue stamped in it: the many tests I did 
      finally were the final straw and I've decided to create four versions 
      of the resender object, so you can pick the one that best fit your needs. 
      To me, it's MantisProcessorActionResendAsHtml.</li>
      </ul>
			
		</div>

<?php
include 'navigation.php';
?>

		<div class="clearer">&nbsp;</div>

	</div>

<?php
include 'footer.php';
?>

</div>

</div>

</body>

</html>
