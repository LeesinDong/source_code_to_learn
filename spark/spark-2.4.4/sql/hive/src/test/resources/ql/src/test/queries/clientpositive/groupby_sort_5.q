set hive.enforce.bucketing = true;
set hive.enforce.sorting = true;
set hive.exec.reducers.max = 10;
set hive.map.groupby.sorted=true;

CREATE TABLE T1(key STRING, val STRING)
CLUSTERED BY (val) SORTED BY (key, val) INTO 2 BUCKETS STORED AS TEXTFILE;

LOAD DATA LOCAL INPATH '../../data/files/T1.txt' INTO TABLE T1;

-- perform an insert to make sure there are 2 files
INSERT OVERWRITE TABLE T1 select key, val from T1;

CREATE TABLE outputTbl1(key STRING, val STRING, cnt INT);

-- The plan should be converted to a map-side group by, since the
-- sorting columns and grouping columns match, and all the bucketing columns
-- are part of sorting columns
EXPLAIN
INSERT OVERWRITE TABLE outputTbl1
SELECT key, val, count(1) FROM T1 GROUP BY key, val;

INSERT OVERWRITE TABLE outputTbl1
SELECT key, val, count(1) FROM T1 GROUP BY key, val;

SELECT * FROM outputTbl1 ORDER BY key, val;

DROP TABLE T1;

CREATE TABLE T1(key STRING, val STRING)
CLUSTERED BY (val, key) SORTED BY (key, val) INTO 2 BUCKETS STORED AS TEXTFILE;

LOAD DATA LOCAL INPATH '../../data/files/T1.txt' INTO TABLE T1;

-- perform an insert to make sure there are 2 files
INSERT OVERWRITE TABLE T1 select key, val from T1;

-- The plan should be converted to a map-side group by, since the
-- sorting columns and grouping columns match, and all the bucketing columns
-- are part of sorting columns
EXPLAIN
INSERT OVERWRITE TABLE outputTbl1
SELECT key, val, count(1) FROM T1 GROUP BY key, val;

INSERT OVERWRITE TABLE outputTbl1
SELECT key, val, count(1) FROM T1 GROUP BY key, val;

SELECT * FROM outputTbl1 ORDER BY key, val;

DROP TABLE T1;

CREATE TABLE T1(key STRING, val STRING)
CLUSTERED BY (val) SORTED BY (key) INTO 2 BUCKETS STORED AS TEXTFILE;

LOAD DATA LOCAL INPATH '../../data/files/T1.txt' INTO TABLE T1;

-- perform an insert to make sure there are 2 files
INSERT OVERWRITE TABLE T1 select key, val from T1;

CREATE TABLE outputTbl2(key STRING, cnt INT);

-- The plan should not be converted to a map-side group by, since although the
-- sorting columns and grouping columns match, all the bucketing columns
-- are not part of sorting columns. However, no hash map aggregation is required
-- on the mapside.
EXPLAIN
INSERT OVERWRITE TABLE outputTbl2
SELECT key, count(1) FROM T1 GROUP BY key;

INSERT OVERWRITE TABLE outputTbl2
SELECT key, count(1) FROM T1 GROUP BY key;

SELECT * FROM outputTbl2 ORDER BY key;

DROP TABLE T1;
