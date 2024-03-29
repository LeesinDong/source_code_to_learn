DROP TABLE part;

-- data setup
CREATE TABLE part( 
    p_partkey INT,
    p_name STRING,
    p_mfgr STRING,
    p_brand STRING,
    p_type STRING,
    p_size INT,
    p_container STRING,
    p_retailprice DOUBLE,
    p_comment STRING
);

LOAD DATA LOCAL INPATH '../../data/files/part_tiny.txt' overwrite into table part;

explain select p1.p_name, p2.p_name
from part p1 , part p2;

explain select p1.p_name, p2.p_name, p3.p_name
from part p1 ,part p2 ,part p3 
where p1.p_name = p2.p_name and p2.p_name = p3.p_name;

explain select p1.p_name, p2.p_name, p3.p_name
from part p1 , (select p_name from part) p2 ,part p3 
where p1.p_name = p2.p_name and p2.p_name = p3.p_name;

explain select p1.p_name, p2.p_name, p3.p_name
from part p1 , part p2 , part p3 
where p2.p_partkey + p1.p_partkey = p1.p_partkey and p3.p_name = p2.p_name;

explain select p1.p_name, p2.p_name, p3.p_name, p4.p_name
from part p1 , part p2 join part p3 on p2.p_name = p1.p_name join part p4 
where p2.p_name = p3.p_name and p1.p_partkey = p4.p_partkey 
            and p1.p_partkey = p2.p_partkey;
            
explain select p1.p_name, p2.p_name, p3.p_name, p4.p_name
from part p1 join part p2 on p2.p_name = p1.p_name , part p3  , part p4 
where p2.p_name = p3.p_name and p1.p_partkey = p4.p_partkey 
            and p1.p_partkey = p2.p_partkey;