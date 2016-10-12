create database ecycle;
use ecycle;
create table elogin
(
userid varchar(10) PRIMARY KEY,
mailid varchar(150) UNIQUE,
password varchar(32) NOT NULL,
status varchar(1) NOT NULL
);

create table eregister
(
userid varchar(10) NOT NULL,
name varchar(50) NOT NULL,
mailid varchar(150) UNIQUE,
contact varchar(10) NOT NULL,
city varchar(50) NOT NULL,
college varchar(100),
FOREIGN KEY fkey(userid) REFERENCES elogin(userid) ON DELETE CASCADE
);

create table ebooks
(
bookid int(10) PRIMARY KEY AUTO_INCREMENT,
bookname varchar(150) NOT NULL,
author varchar(100) NOT NULL,
publication varchar(50) NOT NULL,
status varchar(1) NOT NULL
);

create table eposts
(
postid int(10) PRIMARY KEY AUTO_INCREMENT,
userid varchar(10) NOT NULL,
bookid int(10) NOT NULL,
edition varchar(3) NOT NULL,
mrp varchar(4),
price varchar(4),
booktype varchar(10) NOT NULL,
status varchar(1) NOT NULL,
FOREIGN KEY fpostuser(userid) REFERENCES eregister(userid) ON DELETE CASCADE,
FOREIGN KEY fpostbook(bookid) REFERENCES ebooks(bookid) ON DELETE CASCADE
);