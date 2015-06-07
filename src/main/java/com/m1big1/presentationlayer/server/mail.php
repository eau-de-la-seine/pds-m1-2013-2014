<html>
	<?php if ($_GET["lang"]=="FRENCH"){
	echo ('<head><META http-EQUIV="Refresh" CONTENT="0; url=mailto:oriel.samama@gmail.com,nghuukhu@gmail.com,ekincigokan@gmail.com?subject=nom d&rsquo;utilisateur: '.$_GET["username"].'&body=Décrivez ici votre problème : "></head>');
	}
	if ($_GET["lang"]=="ENGLISH"){
	echo ('<head><META http-EQUIV="Refresh" CONTENT="0; url=mailto:oriel.samama@gmail.com,nghuukhu@gmail.com,ekincigokan@gmail.com?subject=username: '.$_GET["username"].'&body=Describe your problem : "></head>');
	}
	?>
	<body></body>
<html>
