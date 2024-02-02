<?php

require "DataBase.php";
$db = new DataBase();

header('Content-Type: application/json'); // Set the content type to JSON

if (
    isset($_POST['scanned_code']) &&
    isset($_POST['item_description']) &&
    isset($_POST['date_acquired']) &&
    isset($_POST['item_cost']) &&
    isset($_POST['category']) &&
    isset($_POST['status']) &&
    isset($_POST['whereabout']) &&
    isset($_POST['image'])
) {
    if ($db->dbConnect()) {
        $scannedCode = $db->prepareData($_POST['scanned_code']);
        $itemDescription = $db->prepareData($_POST['item_description']);
        $dateAcquired = $db->prepareData($_POST['date_acquired']);
        $itemCost = $db->prepareData($_POST['item_cost']);
        $category = $db->prepareData($_POST['category']);
        $status = $db->prepareData($_POST['status']);
        $whereabout = $db->prepareData($_POST['whereabout']);

        // Convert image data to BLOB format
        $imageData = base64_decode($_POST['image']);

        // Adjust the insertData function to handle additional fields
        if ($db->insertData(
            "scanned_codes",
            $scannedCode,
            $itemDescription,
            $dateAcquired,
            $itemCost,
            $category,
            $status,
            $whereabout,
            $imageData
        )) {
            echo json_encode(array("success" => true, "message" => "Data inserted successfully"));
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
