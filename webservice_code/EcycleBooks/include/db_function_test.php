<?php
class db_function_test
{
    private $conn;
    private $pass;

    //constructor
    function __construct()
    {
        //including database connection file
        require_once 'db_connect.php';
        //connecting to database
        $db = new db_connect();
        $this->conn = $db->connect();
    }

    //destructor
    function __destruct()
    {

    }

    //Storing New user email and generating password for sending on mail id
    public function registerUser($mailid)
    {
        //Generating User Id
        $stmt1 = $this->conn->prepare("Select max(userid) from elogin");
        if ($stmt1->execute()) {
            if ($row = $stmt1->fetch())      //fetching a single row from database
            {
                //incrementing last maximum user id
                $userid = (int)$row['max(userid)'] + 1;
            } else {
                //no user exist yet so initialize userid
                $userid = '1';
            }
        }
        else
        {
            return false;
        }
        $stmt1->close();

        //Generating Random Password
        $this->pass = rand(100000, 100000000);  //creating random password b/w 6-9 digits
        $md5pass = md5($this->pass);        //converting password to md5
        //U represent Unverified User
        //Storing data in database
        $stmt = $this->conn->prepare("insert into elogin values(?,?,?,?)");
        $stmt1 . bindValue('1', $userid);
        $stmt1 . bindValue('2', $mailid);
        $stmt1 . bindValue('3', $md5pass);
        $stmt1 . bindValue('4', 'U');

        if ($stmt->execute()) {
            return true;
            //registered Successfully
            /* $subject="EcycleBooks Registration Details";
             $message="Congratulations, You have been successfully registered as a saviour in eco-friendly campaign Ecycle Books. Your password is-".$this->pass."\n Use this password for first time login. Fill the details and change the password thereafter.\nHave a Good Day!\n\nRegards,\nEcycleBooks Team";
             //mail password to user
             mail($email,$subject,$message,"From: EcycleBooks Team");   //problem in header*/
        } else {
            return false;
            //Failed To register User
        }

    }

//checking if email already exists.
// Return true if mailid Unique.
    public function isUniqueMailId($mailid)
    {
        $stmt1 = $this->conn->prepare("Select mailid from elogin where mailid=?");
        $stmt1 . bindValue('1', $mailid);
        if ($stmt1->execute()) {
            if ($row = $stmt1->fetch()) {
                return false;       //mail id exist in database
            } else {
                return true;        //mail id don't exist in database
            }
        }
    }

}
?>