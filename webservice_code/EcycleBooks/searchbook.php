<?php
require_once './include/db_function.php';

$df=new db_function();

$response=Array("error"=>false);

if(isset($_POST['keyword']))
{
    $keyword=$_POST['keyword'];
    //If no book found
    if(($bookarray=$df->searchbooks($keyword))=="NF")
    {
        $response["error"]=false;
        $response["status"]="notfound";
        echo json_encode($response);
    }
    else if($bookarray=="Error")
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
        $response["books"]=$bookarray;
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