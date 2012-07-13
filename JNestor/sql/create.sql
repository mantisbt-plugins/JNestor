-----
----- creation of database
-----

-- this won't be neccesary, because 
-- jnestor will access to the mantis 
-- tables directly. This table will 
-- be created into the bugtracker database
-- and accessed with the same permissions 
-- as the mantis tables

--create database jnestor;

--grant select, insert, update, delete, alter, drop, create, index  on jnestor.* to jnestor identified by 'PUT-PASSWORD-HERE';


----- 
----- creation of tables
-----

drop table if exists jn_message; 

-- message_id should be primary key, but mysql 
-- doesn't allow primary keys so long, so I left
-- as a simple field

create table jn_message
(
message_id      varchar(1000),
sent            date,
msg_from        varchar(1000),
msg_to          varchar(1000),
subject         text,
body            text
);


