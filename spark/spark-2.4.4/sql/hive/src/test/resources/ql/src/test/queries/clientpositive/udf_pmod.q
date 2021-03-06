set hive.fetch.task.conversion=more;

DESCRIBE FUNCTION pmod;
DESCRIBE FUNCTION EXTENDED pmod;

SELECT pmod(null, null)
FROM src tablesample (1 rows);

SELECT pmod(-100,9), pmod(-50,101), pmod(-1000,29)
FROM src tablesample (1 rows);

SELECT pmod(100,19), pmod(50,125), pmod(300,15)
FROM src tablesample (1 rows);

SELECT pmod(CAST(-100 AS TINYINT),CAST(9 AS TINYINT)), pmod(CAST(-50 AS TINYINT),CAST(101 AS TINYINT)), pmod(CAST(-100 AS TINYINT),CAST(29 AS TINYINT)) FROM src tablesample (1 rows);
SELECT pmod(CAST(-100 AS SMALLINT),CAST(9 AS SMALLINT)), pmod(CAST(-50 AS SMALLINT),CAST(101 AS SMALLINT)), pmod(CAST(-100 AS SMALLINT),CAST(29 AS SMALLINT)) FROM src tablesample (1 rows);
SELECT pmod(CAST(-100 AS BIGINT),CAST(9 AS BIGINT)), pmod(CAST(-50 AS BIGINT),CAST(101 AS BIGINT)), pmod(CAST(-100 AS BIGINT),CAST(29 AS BIGINT)) FROM src tablesample (1 rows);

SELECT pmod(CAST(-100.91 AS FLOAT),CAST(9.8 AS FLOAT)), pmod(CAST(-50.1 AS FLOAT),CAST(101.8 AS FLOAT)), pmod(CAST(-100.91 AS FLOAT),CAST(29.75 AS FLOAT)) FROM src tablesample (1 rows);
SELECT pmod(CAST(-100.91 AS DOUBLE),CAST(9.8 AS DOUBLE)), pmod(CAST(-50.1 AS DOUBLE),CAST(101.8 AS DOUBLE)), pmod(CAST(-100.91 AS DOUBLE),CAST(29.75 AS DOUBLE)) FROM src tablesample (1 rows);
SELECT pmod(CAST(-100.91 AS DECIMAL(5,2)),CAST(9.8 AS DECIMAL(2,1))), pmod(CAST(-50.1 AS DECIMAL(3,1)),CAST(101.8 AS DECIMAL(4,1))), pmod(CAST(-100.91 AS DECIMAL(5,2)),CAST(29.75 AS DECIMAL(4,2))) FROM src tablesample (1 rows);

