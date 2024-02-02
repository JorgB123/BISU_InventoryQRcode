<?php
require "DataBaseConfig.php";

class DataBase
{
    public $connect;
    public $data;
    private $sql;
    protected $servername;
    protected $username;
    protected $password;
    protected $databasename;

    public function __construct()
    {
        $this->connect = null;
        $this->data = null;
        $this->sql = null;
        $dbc = new DataBaseConfig();
        $this->servername = $dbc->servername;
        $this->username = $dbc->username;
        $this->password = $dbc->password;
        $this->databasename = $dbc->databasename;
    }

    function dbConnect()
    {
        $this->connect = mysqli_connect($this->servername, $this->username, $this->password, $this->databasename);
        return $this->connect;
    }

    function prepareData($data)
    {
        return mysqli_real_escape_string($this->connect, stripslashes(htmlspecialchars($data)));
    }

    function logIn($table, $username, $password)
{
    $table = $this->prepareData($table);
    $username = $this->prepareData($username);
    $password = $this->prepareData($password);
    $this->sql = "SELECT * FROM " . $table . " WHERE username = '" . $username . "'";
    $result = mysqli_query($this->connect, $this->sql);
    
    if (mysqli_num_rows($result) != 0) {
        $row = mysqli_fetch_assoc($result);
        $dbusername = $row['username'];
        $dbpassword = $row['password'];

        // Use password_verify to check if the entered password matches the hashed password
        if ($dbusername == $username && password_verify($password, $dbpassword)) {
            $login = true;
        } else {
            $login = false;
        }
    } else {
        $login = false;
    }

    return $login;
}


   function signUp($table, $username, $password)
{
    $username = $this->prepareData($username);
    $password = $this->prepareData($password);
    $hashedPassword = password_hash($password, PASSWORD_DEFAULT);

    // Check if the username already exists
    $checkUsernameQuery = "SELECT * FROM " . $table . " WHERE username = '" . $username . "'";
    $result = mysqli_query($this->connect, $checkUsernameQuery);

    if (mysqli_num_rows($result) > 0) {
        // Username already exists, return false
        return false;
    }

    // Username doesn't exist, proceed with insertion
    $this->sql = "INSERT INTO " . $table . " (username, password) VALUES ('" . $username . "','" . $hashedPassword . "')";

    if (mysqli_query($this->connect, $this->sql)) {
        return true;
    } else {
        return false;
    }
}


// Modify the insertData method to include the item description
function insertData($table, $scannedCode, $itemDescription, $dateAcquired, $itemCost, $category, $status, $whereabout, $imageData)
{
    $table = $this->prepareData($table);
    $scannedCode = $this->prepareData($scannedCode);
    $itemDescription = $this->prepareData($itemDescription);
    $dateAcquired = $this->prepareData($dateAcquired);
    $itemCost = $this->prepareData($itemCost);
    $category = $this->prepareData($category);
    $status = $this->prepareData($status);
    $whereabout = $this->prepareData($whereabout);

    // Convert image data to BLOB format
    $imageData = mysqli_real_escape_string($this->connect, base64_decode($imageData));

    // Construct SQL query to insert data, including image BLOB and new fields
    $sql = "INSERT INTO " . $table . " (scanned_code, item_description, date_acquired, item_cost, category, status, whereabout, image) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    $stmt = $this->connect->prepare($sql);
    $stmt->bind_param("ssssssss", $scannedCode, $itemDescription, $dateAcquired, $itemCost, $category, $status, $whereabout, $imageData);

    if ($stmt->execute()) { 
        $stmt->close();
        return true;
    } else {
        $stmt->close();
        return false;
    }
}

    function insertIPAddress($table,$IPAddress)
{
    $table = $this->prepareData($table);
    $IPAddress = $this->prepareData($IPAddress);

    // Construct SQL query to insert data, including image BLOB and new fields
    $sql = "INSERT INTO " . $table . " (IPAddress) VALUES (?)";

    $stmt = $this->connect->prepare($sql);
    $stmt->bind_param("s", $IPAddress);

    if ($stmt->execute()) { 
        $stmt->close();
        return true;
    } else {
        $stmt->close();
        return false;
    }
}

function fetchIPAddress($tables)
{
    $table = $this->prepareData($table);
    $username = $this->prepareData($IPAddress);
    $this->sql = "SELECT IPAddress FROM " . $table . " WHERE ID =1";
    $result = mysqli_query($this->connect, $this->sql);
    
    if (mysqli_num_rows($result) != 0) {
        $row = mysqli_fetch_assoc($result);
        $dbIPAddress = $row['IPAddress'];

        if (empty($IPAddress)) {
            $exist = false;
        } else {
            $exist = true;
        }

    } else {
        $exist = false;
    }

    return $exist;
}










function getUserData($table, $username)
{
    $username = $this->prepareData($username);
    $this->sql = "SELECT image_path, username FROM " . $table . " WHERE username = '" . $username . "'";
    $result = mysqli_query($this->connect, $this->sql);

    if (mysqli_num_rows($result) > 0) {
        $row = mysqli_fetch_assoc($result);
        $image_path = $row['image_path'];
        $userData = array('image_path' => $image_path, 'username' => $username);

        return $userData;
    } else {
        return null; // User not found
    }
}



}











?>
