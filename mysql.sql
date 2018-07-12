CREATE DATABASE external_people_db;
CREATE TABLE people
        (date_event     timestamp NOT NULL,
         first_name     varchar(255)    NOT NULL,
         is_arrival     boolean NOT NULL,
         second_name    varchar(255) NOT NULL,
         color_hair     varchar(255)  NOT NULL,
         PRIMARY KEY (first_name, second_name));