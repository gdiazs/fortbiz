FortBiz DB
===================

Project based on Flyway. The main idea of use flyway, is to simplify the database migration and looking for automatiziation by command line.




Requirements
-------------

 - JDK 11
 - Maven 3.x
 - PostgreSQL 11.x


Developer command line
-------------
In order to migrate this project locally you will need to run next commands

Drop and create dadtabase

     mvn flyway:clean flyway:migrate

Migrate incremental only:

	 flyway:migrate
