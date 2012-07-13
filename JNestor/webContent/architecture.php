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
			
			<a href="index.html">Home</a> &#8250; <a href="index.html">Subpage</a>

	</div>

	<div class="main">		
		
		<div class="content">

			<h1>Components out of the shelf</h1>

			<p>Most of <b>JNestor</b> is developed with frameworks and libraries out-of-the-shelf.
			The only exception is the <a href="http://www.futureware.biz/mantisconnect/">MantisCoNNect 
			library</a> which is not free for comercial issue. Visit their website for 
			details about licensing.</p>
			
			<p>
			The rest of libraries are of free use or free software:</p>
			
			<ul>
			<li><em>JavaMail</em> for reading and writing emails.</li>
			<li><em>Jsoup</em> for processing the HTML content of the emails, if needed.</li>
			<li><em>Spring framework</em> for the injection of objects. I do an intensive use 
			of spring applicationContext.xml file, even for the configuration of the 
			application. This leverages the hassle of creating config code into the application, 
			which would be transversal to all the objects in the application, making the 
			the objects of the application more dependents among them.</li>
            <li><em>commons logging</em> of apache to isolate from a particular 
            logging implementation
			<li><em>log4j</em> for logging
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
