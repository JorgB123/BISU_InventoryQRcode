<?php
require "DataBase.php";
$db = new DataBase();

 
$username  = $_GET['username'];
 
$sql = "select * from users where id like '%$username%'";
 
$res = mysqli_query($db,$sql);
 
$result = array();
 
while($row = mysqli_fetch_array($res)){
array_push($result,array('image_path'=>$row[5]

));
}
 
echo json_encode(array("result"=>$result));
 
mysqli_close($con);
 
?>
