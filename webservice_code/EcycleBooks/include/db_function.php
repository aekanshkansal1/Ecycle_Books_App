<?php
class db_function
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

    //Storing New user email and generating password for sending on mail id.
    //Return true if user stored successfully else false.
    public function registerUser($mailid)
    {
        //Future Scope:Using Auto increment directly in db and adapting db for Facebook, Google+ login
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


        //Generating Random Password
        $this->pass = rand(100000, 100000000);  //creating random password b/w 6-9 digits
        $md5pass = md5($this->pass);        //converting password to md5
        //U represent Unverified User
        //Storing data in database
        $stmt = $this->conn->prepare("insert into elogin values(?,?,?,?)");
        /*       $stmt . bindValue(1, $userid);
               $stmt . bindValue(2, $mailid);
               $stmt . bindValue(3, $md5pass);
               $stmt . bindValue(4, 'U');*/
        if ($stmt->execute(array($userid,$mailid,$md5pass,'U'))) {
            //registered Successfully
            $subject="EcycleBooks Registration Details";
            $message="Congratulations,\nYou have been successfully registered as a saviour in eco-friendly campaign Ecycle Books. Your password is-".$this->pass."\nUse this password for first time login. Fill the details and change the password thereafter.\nHave a Good Day!\n\nRegards,\nEcycleBooks Team";
            //mail password to user
            mail($mailid,$subject,$message,"From: ecyclebooks@gmail.com");
            return true;
        } else {
            return false;
            //Failed To register User
        }

    }

//checking if email already exists.
// Return true if mailid Unique.
    public function isUniqueMailId($mailid)
    {
        $stmt2 = $this->conn->prepare("Select mailid from elogin where mailid=?");
        if ($stmt2->execute(array($mailid))) {
            if ($row = $stmt2->fetch()) {
                return false;       //mail id exist in database
            } else {
                return true;        //mail id don't exist in database
            }
        }
    }

    //Check user authentication as per mail id and password and send the userid back.
    public function checkLogin($mailid,$password)
    {
        $stmt2 = $this->conn->prepare("Select userid,status from elogin where mailid=? and password=?");
        if ($stmt2->execute(array($mailid,$password))) {
            if ($row = $stmt2->fetch()) {
                return array("userid"=>$row['userid'],"userstatus"=>$row['status']);       //If any record corressponding mail id,password exist in database
            } else {
                return -1;        //if mail id,password don't exist in database
            }
        }
    }

    //Storing user data and updating status/password.
    public function storeInfo($userid,$name,$password,$contact,$city,$school)
    {
        try
        {
            //create transaction and store data in eregister. update password and status in elogin.
            $this->conn->beginTransaction();
            $stmt2=$this->conn->prepare("Select mailid from elogin where userid=?");
            $stmt2->execute(array($userid));
            $mailid=$stmt2->fetchColumn();   //getting single value from single row
            //to get array of data in colm use $stmt->fetchAll(PDO::FETCH_COLUMN), for single row use fetch()
            $stmt3 = $this->conn->prepare("Insert into eregister values(?,?,?,?,?,?)");
            $stmt3->execute(array($userid,$name,$mailid,$contact,$city,$school));
            $stmt4=$this->conn->prepare("Update elogin set password=?,status=? where userid=?");
            $stmt4->execute(array($password,'V',$userid));
            $this->conn->commit();
            return true;
        }
        catch(Exception $e)
        {
            $this->conn->rollback();
            return false;
        }

    }

    public function searchbooks($keyword)
    {
        $stmt=$this->conn->prepare("select * from ebooks where bookname LIKE '%$keyword%'");
        if($stmt->execute(array($keyword)))
        {
            //If rows found in database return all of them as associative array
            if($rows=$stmt->fetchAll(PDO::FETCH_ASSOC))
            {
                return $rows;
            }
            else
            {
                return "NF";
            }
        }
        else
        {
            return "Error";
        }
    }

    public function sellBooks($userid,$bookid,$mrp,$price,$edition)
    {
        $stmt = $this->conn->prepare("insert into eposts values(?,?,?,?,?,?,?,?)");
            if ($stmt->execute(array(0,$userid, $bookid, $edition, $mrp, $price, 'used', 'U'))) {
                return true;
            } else {
                return false;
                //Failed To add Post
            }

        }

    public function donateBooks($userid,$bookid,$edition)
    {
        $stmt = $this->conn->prepare("insert into eposts values(?,?,?,?,?,?,?,?)");
        if ($stmt->execute(array(0,$userid, $bookid, $edition, 'NA', 'NA',  'donated', 'U'))) {
            return true;
        } else {
            return false;
            //Failed To add Post
        }

    }

    public function duplicatePosts($userid,$bookid,$edition,$booktype)
    {
        $stmt = $this->conn->prepare("select edition from eposts where userid=? and bookid=? and booktype=?");
        if($stmt->execute(array($userid,$bookid,$booktype)))
        {
            if($row=$stmt->fetch())
            {
              if($row['edition']==$edition) {
                  return true;
              }
                else
                {
                return false;
                }
            }
            else
            {
                return false;
            }
        }
        else
        {
            return -1;
        }
    }

    public function addBooks($name,$author,$publication)
    {
        $stmt = $this->conn->prepare("insert into ebooks values(?,?,?,?,?)");
        //book id is auto increment field
        if ($stmt->execute(array(0, $name,$author,$publication,'U'))) {
            return true;
        } else {
            return false;
            //Failed To add Post
        }

    }

    public function duplicateBook($bookname,$bookauthor,$publication)
    {
        $stmt = $this->conn->prepare("select bookid from ebooks where bookname=? and author=? and publication=?");
        if($stmt->execute(array($bookname,$bookauthor,$publication)))
        {
            if($row=$stmt->fetch())
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return -1;
        }
    }

    public function listbooks($bookid)
    {
        $flag=true;
        $stmt=$this->conn->prepare("select userid,edition,mrp,price,booktype from eposts where bookid=?");
        if($stmt->execute(array($bookid)))
        {
                $assoc=array();

                //START OF WHILE
                while ($row = $stmt->fetch(PDO::FETCH_ASSOC)) {
                $flag=false;
                $stmt2=$this->conn->prepare("select city,college from eregister where userid=?");
                //appending additional data in each row of associative array
                if($stmt2->execute(array($row['userid'])))
                {
                  $userinfo=$stmt2->fetch();
                  $row['city']=$userinfo['city'];
                  $row['college']=$userinfo['college'];
                
                  $assoc[]=$row;
                }
                else
                {
                    return "Error";                    
                }
                }    //END OF WHILE

                //if no data got
                if($flag)
                {
                    return "NF";
                }
                else
                {
                    return $assoc;    //we got data
                }
        }
        else
        {
            return "Error";
        }
    }

public function getContact($userid)
    {
        $stmt2 = $this->conn->prepare("Select contact,mailid from eregister where userid=?");
        if ($stmt2->execute(array($userid))) {
            if ($row = $stmt2->fetch()) {
                return array("contact"=>$row['contact'],"mailid"=>$row['mailid']);
            } 
        }
        else {
                return -1;        //if mail id,password don't exist in database
        }
    }


}
?>