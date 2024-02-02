<?php

class DataBaseConfig
{
    public $servername;
    public $username;
    public $password;
    public $databasename;

    public function __construct()
    {

        $this->servername = 'localhost';
        $this->username = 'glenn';
        $this->password = 'password';
        $this->databasename = 'loginregister';

    }
}

?>
