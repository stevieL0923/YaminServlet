drop database if exists yamindb;

DROP USER 'student'@'localhost';

create database yamindb;

use yamindb;

CREATE USER 'student'@'localhost' IDENTIFIED BY 'Java#1Rules';

GRANT ALL PRIVILEGES ON yamindb.* TO 'student'@'localhost' ;

-- ---------------------------------------------------------------------------
-- Create participant table representing participant 
-- ---------------------------------------------------------------------------
create table participant
    (participant_id       smallint            not null   auto_increment, 
     full_name            char(32)            not null,
     email                varchar(32)         not null,
     phone                varchar(32)         not null,
     morningSession       boolean,
     primary key(participant_id));

-- ---------------------------------------------------------------------------
-- Add data to batch participant
-- ---------------------------------------------------------------------------
insert into participant (full_name, email, phone, morningSession) values('Tom Jones',     'tom@work.com',   '323-321-4321', true);
insert into participant (full_name, email, phone, morningSession) values('Frank Sinatra', 'frank@work.com', '626-444-4444', true);
insert into participant (full_name, email, phone, morningSession) values('Tony Bennett',  'tony@work.com',  '858-888-8888', false);

-- ---------------------------------------------------------------------------
-- Create batch table representing batch 
-- ---------------------------------------------------------------------------
 create table batch
   (batch_id             smallint            not null,
	timeSlot     		 smallint            not null,
	morning              boolean             not null,
	primary key(batch_id)
 );

-- ---------------------------------------------------------------------------
-- Add data to batch table
-- ---------------------------------------------------------------------------
insert into batch values(7, 7, true);
insert into batch values(8, 8, true);
insert into batch values(9, 9, true);
insert into batch values(17, 5, false);
insert into batch values(18, 6, false);
insert into batch values(19, 7, false);	   

-- ---------------------------------------------------------------------------
-- Create enrollment table representing enrollment 
-- ---------------------------------------------------------------------------
 create table enrollment
	 (id                   smallint            not null   auto_increment,
	  participant_id       smallint            not null,
	  batch_id             smallint            not null,
	  primary key(id),
	  foreign key(participant_id) references participant(participant_id)
	                              on delete cascade,
	  foreign key(batch_id) references batch(batch_id)
	                              on delete cascade);
     
-- Display all the databases mySql knows about
show databases;

-- Display current database
select DATABASE() as Current_Database;

-- Display all the users and the host mySql server thay are defined on
select user, host from mysql.user;

-- Display the privileges help by the user defined for class work
show grants for 'student'@'localhost';

-- Disply the port the mySQL server is monitoring
SHOW GLOBAL VARIABLES LIKE 'PORT';

-- Display all the tables in the current database
show tables;

-- Display the contents of the tables created for class work
select * from participant;
select * from batch;
select * from enrollment;

            