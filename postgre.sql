CREATE DATABASE internal_people_db;
CREATE TABLE people_in_town
        (changed_datetime     timestamp NOT NULL,
         first_name     text    NOT NULL,
         second_name    text NOT NULL,
         color_hair     text  NOT NULL,
         agent_id text NOT NULL,
         PRIMARY KEY (first_name, second_name, agent_id));

 CREATE DATABASE external_people_db;
 CREATE TABLE people
         (date_event     timestamp NOT NULL,
         first_name     text    NOT NULL,
         is_arrival     boolean NOT NULL,
         second_name    text NOT NULL,
         color_hair     text  NOT NULL,
         PRIMARY KEY (first_name, second_name));