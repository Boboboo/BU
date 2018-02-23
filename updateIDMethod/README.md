## Update ID Method

Sometimes due to job demand,  I need to put id (primary key) only in the first column, not last column after populating data.
so I need to recreate table.</br>


#### 1. create table links_temp
create table links_temp(</br>
        id                 int,</br>
	bio_entity_1       integer,</br> 
	entity_1_name      character varying(255),</br>
	bio_entity_2       integer,</br>
	entity_2_name      character varying(255),</br> 
	entity_1_type      character varying(255),</br>
	entity_2_type      character varying(255),</br>
	contextid          character varying(255),</br> 
	interaction_type   character varying(255),</br>
	habitat_1          character varying(255),</br> 
	habitat_2          character varying(255),</br>
	pvalue             double precision,</br>
	weight             double precision,</br>
	annotation         character varying(255)</br>
 )</br>
 
 
 
 #### 2. use the program to populate data to links_temp
 
 
 
 #### 3. export data from links_temp
 \copy links_temp to '/Users/air/Desktop/temp.csv';</br>
 
 
 
 #### 4. create table links, and clarify primary key at this moment
create table links(</br>
        id                 serial primary key  not null,</br>
	bio_entity_1       integer,</br> 
	entity_1_name      character varying(255),</br>
	bio_entity_2       integer,</br>
	entity_2_name      character varying(255),</br> 
	entity_1_type      character varying(255),</br>
	entity_2_type      character varying(255),</br>
	contextid          character varying(255),</br> 
	interaction_type   character varying(255),</br>
	habitat_1          character varying(255),</br> 
	habitat_2          character varying(255),</br>
	pvalue             double precision,</br>
	weight             double precision,</br>
	annotation         character varying(255)</br>
 )</br>
 
 
 
 #### 5. import data to links
 \copy links from '/Users/air/Desktop/temp.csv' with DELIMITER E'\t';
 
