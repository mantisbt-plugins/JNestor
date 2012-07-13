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

			<h1>Another Majordomo in Java</h1>

			<p><b>JNestor</b> is a software developed in java whose main purpose is to 
			read a mailbox and perform some automatic functions. I've started this project
			with the idea of serving as an interface between <a href="http://www.mantisbt.org">Manits
			Bug Tracker</a> and any mailbox.</p>
			
			<p>
			Altough there are plugins that can be installed in mantis to do such tasks, I've 
			found that this plugins doesn't fullfill all my expectations, one of the most 
			serious is that these plugins doesn't have the capability to set the issue number
			back to he email client.</p>
			
			<p>
			As a Mantis majordomo, the main features are: 
			</p>
			
			<ul>
			<li><em>Flexible:</em> It can be configured to answer every 
			every email received, send a copy to other email account, 
			delete the original message or archive it. This is thanks 
			to the </li>
			<li><em>Extensible:</em> Instead of entering new issues in mantis, 
			you can develop your own processors and add use it to store form 
			information into a database, or perform some actions if specific 
			commands are sent in the email, or add new issues to Bugzilla 
			instead of adding them to Mantis
			<li><em>Portable:</em> Because it's developed in Java&tm; you can 
			bet that this software runs smoothly in Windows, Linux and a dozen 
			platforms outthere
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