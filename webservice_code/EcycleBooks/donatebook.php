<?php
require_once './include/db_function.php';

$df=new db_function();

$response=Array("error"=>false);

//If school is not there it will get NA
if(isset($_POST['userid']) && isset($_POST['edition']) && isset($_POST['bookname']) && isset($_POST['author']) && 
    isset($_POST['publication'])) {
    $userid = $_POST['userid'];
    $bookname=$_POST['bookname'];
    $author=$_POST['author'];
    $publication=$_POST['publication'];
    $edition = $_POST['edition'];
    if($publication=="")
        $publication="NA";
    
    if ($df->donateBooks($userid,$bookname,$author,$publication,$edition))
    {
            $response["error"] = false;
            $response["status"] = "success";
            echo json_encode($response);
    }
    else 
    {
            $response["error"] = true;
            $response["status"] = "failure";
            $response["error_msg"] = "Error in Posting Data.";
            echo json_encode($response);
    }
}
else
{
    $response["error"]=true;
    $response["status"]="failure";
    $response["error_msg"]="Fields are missing.";
    echo json_encode($response);
}
?>