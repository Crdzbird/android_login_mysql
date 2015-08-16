<?PHP
error_reporting(0);
include_once("connection.php");

if(isset($_GET['action']) == "delete" && isset($_GET['did'])){
	$did = $_GET['did'];
	mysql_query("DELETE FROM tbl_movie WHERE m_id = $did");
}

$result = mysql_query("SELECT m_id, m_name, m_dim, m_reldate, m_rate FROM tbl_movie");

if(isset($_GET['format']) && $_GET['format']=="json"){
	while($row = mysql_fetch_assoc($result)){
		$output[] = $row;
	}
	print (json_encode($output));
}
else{
?>

<html>
<head>
</head>
<script>
function confirmDelete(delUrl) {
  if (confirm("Are you sure to delete it?")) {
    document.location = delUrl;
  }
}
</script>
<body>
	<a href="addnew.php">Add New</a><br/>
	<table id="table1" class="display" border="1">
		<thead>
		<tr>
			<td>Movie ID</td>
			<td>Name</td>
			<td>Dimension</td>
			<td>Release Date</td>
			<td>Rate</td>	
			<td>Action</td>
			
		</tr>
		</thead>
		<tbody>
		<?PHP while(($row = mysql_fetch_assoc($result)) == true){ ?>
			<tr>
				<td><?PHP echo $row['m_id']; ?></td>
				<td><?PHP echo $row['m_name']; ?></td>
				<td><?PHP echo $row['m_dim']; ?></td>
				<td><?PHP echo $row['m_reldate']; ?></td>
				<td><?PHP echo $row['m_rate']; ?></td>
				<td>
					<a href="edit.php?id=<?PHP echo $row['m_id']; ?>">Edit</a> 
					<a href=javascript:confirmDelete("index.php?action=delete&did=<?PHP echo $row['m_id']; ?>")>Delete</a>
				</td>
				
			</tr>
		<?PHP } ?>
		</tbody>
	</table>
</body>
<html>
<?PHP
}
?>