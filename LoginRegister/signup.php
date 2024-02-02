<?php
require "DataBase.php";
$db = new DataBase();
if (isset($_POST['username']) && isset($_POST['password'])) {
    if ($db->dbConnect()) {
        if ($db->signUp("users", $_POST['username'], $_POST['password'])) {
            echo "Sign Up Success";
        } else echo "Sign Up Failed";//already exist
    } else echo "Error: Database connection";
} else echo "All fields are required";
?>
