<?php
require_once './include/db_function.php';

$df=new db_function();

$response=Array("error"=>false);

//If school is not there it will get NA
if(isset($_POST['bookname']) && isset($_POST['author']) && isset($_POST['publication'])) {
    $bookname = $_POST['bookname'];
    $author = $_POST['author'];
    $publication = $_POST['publication'];

    if (($duplicate = $df->duplicateBook($bookname, $author,$publication)) == true) {
        $response["error"] = true;
        $response["status"] = "failure";
        $response["error_msg"] = "Already Added Book.";
        echo json_encode($response);
    }
    else if ($duplicate == -1) {
        $response["error"] = true;
        $response["status"] = "failure";
        $response["error_msg"] = "Error in Posting Data.";
        echo json_encode($response);
    }
    else {
        if ($df->addBooks($bookname, $author, $publication)) {
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