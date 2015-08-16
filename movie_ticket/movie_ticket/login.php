<?PHP
error_reporting(E_WARNING);
include_once("connection.php");

if(	isset($_POST['txtUsername']) &&
	isset($_POST['txtPassword']) 
	)
{
	$result = mysql_query("SELECT user_name, password FROM tbl_user ".
			" WHERE user_name = '". $_POST['txtUsername'] ."' AND " . 
			" password = '" . $_POST['txtPassword']. "'");	
	
	if(mysql_num_rows($result) > 0)
	{	
		if($_POST['mobile'] == "android"){
			echo "login_success";
			exit;
		}
		
		header("location: index.php");		
	}
	else{
		echo "Login Failed <br/>";
	}	
}				
?>
<html>
<head>
<title>
</title>
</head>
<body>
	<a href="index.php">Back to Home</a>
	<form action="<?PHP $_PHP_SELF ?>" method="post">
		Username <input type="text" name="txtUsername" value="" ><br/>
        Password <input type="password" name="txtPassword" value="" ><br/>
		<input type="submit" name="btnSubmit" value="Login">
	</form>
</body>
</html>