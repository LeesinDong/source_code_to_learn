CREATE DATABASE db1;
DESCRIBE DATABASE EXTENDED db1;

USE db1;
CREATE TABLE table_db1 (name STRING, value INT);

DESCRIBE FORMATTED table_db1;
SHOW TABLES;

CREATE DATABASE db2
COMMENT 'database 2'
LOCATION '${hiveconf:hive.metastore.warehouse.dir}/db2';

DESCRIBE DATABASE EXTENDED db2;

USE db2;
CREATE TABLE table_db2 (name STRING, value INT);

DESCRIBE FORMATTED table_db2;
SHOW TABLES;
