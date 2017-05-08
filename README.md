# Ecycle_Books_App
Update- New version of app under development currently.

This project is provided here for reference/contribution.

Mobile and Web Application Project to recycle old books. People can sell, donate and purchase books through the application.
Technology used are Java, Android, PHP, MySQL, JSON

Android app and Web Services completed. Website work ongoing.
Application work only when internet available and webservices must be uploaded on a centralized server.

Webservices are contained in folder webservice_code/EcycleBooks

To setup the project

->Go to include folder in webservice_code and within config.php change the parameters to connect to database. Give the name of database and your database username(default username "root" in mysql) and password(default there is no password in MySQL so leave it blank) as well as the host name(localhost works in most of the cases)
->Change the database name as used in config file in create as well as use query. import the db.sql file in your database of hosting
->upload all the web services on the hosting/server

Android App Code is contained in folder app_code/EcycleBooks

->Go to the src/com/aekanshkansal/ecyclebooks and update the URL like REGISTER_URL,LOGIN_URL and similar in java activity files with the exact address of the web services i.e. the webpage locations as per website on which you have hosted.
->Rebuild the application and use

The final working apk is included which is using the webservices and database from online server.
