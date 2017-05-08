create database database_name;
use database_name;
create table elogin
(
userid varchar(20) PRIMARY KEY,
mailid varchar(150) UNIQUE,
password varchar(32) NOT NULL,
status varchar(1) NOT NULL
);

create table eregister
(
userid varchar(20) NOT NULL,
name varchar(50) NOT NULL,
mailid varchar(150) UNIQUE,
contact varchar(10) NOT NULL,
city varchar(50) NOT NULL,
college varchar(100),
FOREIGN KEY fkey(userid) REFERENCES elogin(userid) ON DELETE CASCADE
);

create table eposts
(
postid int(20) PRIMARY KEY AUTO_INCREMENT,
userid varchar(20) NOT NULL,
bookname varchar(150) NOT NULL,
author varchar(150) NOT NULL,
publication varchar(150) NOT NULL,
edition varchar(8) NOT NULL,
mrp varchar(5),
price varchar(5),
booktype varchar(10) NOT NULL,
status varchar(1) NOT NULL,
FOREIGN KEY fpostuser(userid) REFERENCES eregister(userid) ON DELETE CASCADE
);