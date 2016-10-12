<?php
require_once './include/db_function.php';

$df=new db_function();

$response=Array("error"=>false);

if(isset($_POST['bookid']))
{
    $bookid=$_POST['bookid'];
    //If no book found
    if(($postarray=$df->listbooks($bookid))=="NF")
    {
        $response["error"]=false;
        $response["status"]="notfound";
        echo json_encode($response);
    }
    else if($postarray=="Error")
    {
        $response["error"]=true;
        $response["status"]="failure";
        $response["error_msg"]="Error in Searching.";
        echo json_encode($response);
    }
    else
    {
        $response["error"]=false;
        $response["status"]="success";
        $response["posts"]=$postarray;
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