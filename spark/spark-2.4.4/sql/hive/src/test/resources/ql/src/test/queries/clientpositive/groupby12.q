set hive.map.aggr=false;

CREATE TABLE dest1(key INT, value STRING) STORED AS TEXTFILE;

EXPLAIN
FROM src
INSERT OVERWRITE TABLE dest1 SELECT COUNT(src.key), COUNT(DISTINCT value) GROUP BY src.key;

FROM src
INSERT OVERWRITE TABLE dest1 SELECT COUNT(src.key), COUNT(DISTINCT value) GROUP BY src.key;

SELECT dest1.* FROM dest1;

