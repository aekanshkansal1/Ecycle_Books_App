<?php
require_once './include/db_function.php';

$df=new db_function();

$response=Array("error"=>false);

//If school is not there it will get NA
if(isset($_POST['userid']) && isset($_POST['bookname']) && isset($_POST['author']) && isset($_POST['publication']) && 
    isset($_POST['mrp']) && isset($_POST['price']) && isset($_POST['edition'])) {
    $userid = $_POST['userid'];
    $bookname = $_POST['bookname'];
    $author=$_POST['author'];
    $publication=$_POST['publication'];
    $mrp = $_POST['mrp'];
    $price = $_POST['price'];
    $edition = $_POST['edition'];

        if($publication=="")
            $publication="NA";

        if ($df->sellBooks($userid, $bookname,$author,$publication,$mrp,$price,$edition)) {
            $response["error"] = false;
            $response["status"] = "success";
            echo json_encode($response);
        }
        else {
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