<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['IPAddress'])) {
    if ($db->logIn("ipaddress")) {
         echo "IP exists";
     } else echo "IP does not exists";
} else echo "All fields are required";
?>