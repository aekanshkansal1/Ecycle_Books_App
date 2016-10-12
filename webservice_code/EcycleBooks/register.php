<?php
include_once './include/db_function.php';
//object of data function class
$df=new db_function();

//Json Response array
$response=array("error"=>false);

//checking email and validating
if(isset($_POST['mailid']))
{
    $mailid=$_POST['mailid'];
    //mail id unique.returns true if it is unique.
    if($df->isUniqueMailId($mailid))
    {
        //returns true if register successfully
        $user = $df->registerUser($mailid);
        if($user)
        {
            $response["error"]=false;
            $response["status"]="success";
            echo json_encode($response);
        }
        else
        {
            $response["error"]=true;
            $response["status"]="failure";
            $response["error_msg"]="Error in Registering User.Please try again.";
            echo json_encode($response);
        }

    }
    else
    {
        $response["error"]=true;
        $response["status"]="failure";
        $response["error_msg"]="Mailid already registered";
        echo json_encode($response);
    }
}
else
{
        $response["error"]=true;
        $response["status"]="failure";
        $response["error_msg"]="Mailid is missing";
        echo json_encode($response);
}
?>

