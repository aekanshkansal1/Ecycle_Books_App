<?php
class db_connect
{
    private $conn;

    //connect to db
    public function connect()
    {
        //including constants
        require_once 'config.php';
        try {
            $this->conn = new pdo("mysql:host=".DB_HOST.";dbname=".DB_NAME.";charset=utf8;",DB_USER,DB_PASSWORD);
            $this->conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            return $this->conn;
        }
        catch(Exception $ex)
        {
            echo "Error in database Connection";
        }
    }
}
?>