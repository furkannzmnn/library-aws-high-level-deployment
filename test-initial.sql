# create lib database
create database lib;

# create book table
create table book (
    id int not null auto_increment,
    title varchar(255) not null,
    author varchar(255) not null,
    primary key (id)
);