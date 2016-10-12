<?php
require_once './include/db_function.php';

$df=new db_function();

$response=Array("error"=>false);

//If school is not there it will get NA
if(isset($_POST['userid']) && isset($_POST['bookid']) && isset($_POST['edition'])) {
    $userid = $_POST['userid'];
    $bookid = $_POST['bookid'];
    $edition = $_POST['edition'];

    if (($duplicate = $df->duplicatePosts($userid, $bookid, $edition,'donated')) == true) {
        $response["error"] = true;
        $response["status"] = "failure";
        $response["error_msg"] = "Already Posted Book For Sale.";
        echo json_encode($response);
    }
    else if ($duplicate == -1) {
        $response["error"] = true;
        $response["status"] = "failure";
        $response["error_msg"] = "Error in Posting Data.";
        echo json_encode($response);
    }
    else {
        if ($df->donateBooks($userid, $bookid, $edition)) {
            $response["error"] = false;
            $response["status"] = "success";
            echo json_encode($response);
        }
        else {
            $response["error"] = true;
            $response["status"] = "failure";
            $response["error_msg"] = "Error in Posting Data.";
            echo json_encode($response);        }
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