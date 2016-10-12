<?php
require_once './include/db_function.php';

$df=new db_function();

$response=Array("error"=>false);

//If school is not there it will get NA
if(isset($_POST['userid']) && isset($_POST['name']) && isset($_POST['password'])&& isset($_POST['contact']) && isset($_POST['city']) && isset($_POST['school']))
{
    $userid=$_POST['userid'];
    $name=$_POST['name'];
    $password=$_POST['password'];
    $contact=$_POST['contact'];
    $city=$_POST['city'];
    $school=$_POST['school'];
    $password=md5($password);
    if($df->storeInfo($userid,$name,$password,$contact,$city,$school))
    {
        $response["error"]=false;
        $response["status"]="success";
        echo json_encode($response);
    }
    else
    {
        $response["error"]=true;
        $response["status"]="failure";
        $response["error_msg"]="Error in Updating Profile.";
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