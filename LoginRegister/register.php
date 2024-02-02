<?php
require "DataBase.php";
$db = new DataBase();

if (isset($_POST['fullname']) && isset($_POST['address']) && isset($_POST['username']) && isset($_POST['password']) && isset($_POST['image'])) {
    if ($db->dbConnect()) {
        $fullname = $_POST['fullname'];
        $address = $_POST['address'];
        $username = $_POST['username'];
        $password = $_POST['password'];
        $imageData = $_POST['image']; // Base64 encoded image data

        // Decode Base64 image data
        $decodedImage = base64_decode($imageData);

        // Define the absolute server file path to store the uploaded images
        $uploadDirectory = 'uploads/'; // Change this to your desired image directory
        $imageFileName = $username . '.jpg'; // Rename the image based on the username
        $uploadPath = $uploadDirectory . $imageFileName;

        // Save the image to the server
        if (file_put_contents($uploadPath, $decodedImage)) {
            // Image saved successfully
            // Now, you can insert the user registration data and image path into your MySQL database

            if ($db->registration("users", $fullname, $address, $username, $password, $uploadPath)) {
                echo "Successfully Registered";
            } else {
                echo "Error inserting user registration data into the database.";
            }
        } else {
            // Handle image upload error
            echo "Error uploading image.";
        }
    } else {
        echo "Error: Database connection";
    }
} else {
    echo "All fields are required";
}
?>
