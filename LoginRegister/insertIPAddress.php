<?php

require "DataBase.php";
$db = new DataBase();

header('Content-Type: application/json'); // Set the content type to JSON

if (
    isset($_POST['IPAddress'])
) {
    if ($db->dbConnect()) {
        $IPAddress = $db->prepareData($_POST['IPAddress']);
        

   

        // Adjust the insertData function to handle additional fields
        if ($db->insertIPAddress(
            "ipaddress",
            $IPAddress
            
        )) {
            echo "Data inserted successfully";
        } else {
            echo json_encode(array("success" => false, "message" => "Error: Failed to insert data"));
        }
    } else {
        echo json_encode(array("success" => false, "message" => "Error: Database connection"));
    }
} else {
    echo json_encode(array("success" => false, "message" => "Incomplete data"));
}
?>
