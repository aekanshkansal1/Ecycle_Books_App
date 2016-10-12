<?php
require_once './include/db_function.php';

$df=new db_function();

$response=Array("error"=>false);

if(isset($_POST['userid']))
{
    $userid=$_POST['userid'];
    if(($array=$df->getContact($userid))!=-1)
    {
        $response["error"]=false;
        $response["status"]="success";
        $response["contact"]=$array["contact"];
        $response["mailid"]=$array["mailid"];
        echo json_encode($response);
    }
    else
    {
        $response["error"]=true;
        $response["status"]="failure";
        $response["error_msg"]="Error in Getting Details.";
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