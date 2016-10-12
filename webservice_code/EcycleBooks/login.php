<?php
require_once './include/db_function.php';

$df=new db_function();

$response=Array("error"=>false);

if(isset($_POST['mailid']) && isset($_POST['password']))
{
    $mailid=$_POST['mailid'];
    $password=$_POST['password'];
    $password=md5($password);
    if(($array=$df->checkLogin($mailid,$password))!=-1)
    {
        $response["error"]=false;
        $response["status"]="success";
        $response["userid"]=$array["userid"];
        $response["userstatus"]=$array["userstatus"];
        echo json_encode($response);
    }
    else
    {
        $response["error"]=true;
        $response["status"]="failure";
        $response["error_msg"]="Invalid Login Credentials.";
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