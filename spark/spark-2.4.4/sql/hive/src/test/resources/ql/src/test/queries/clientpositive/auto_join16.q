
set hive.auto.convert.join = true;


explain
SELECT sum(hash(subq.key, tab.value)) 
FROM 
(select a.key, a.value from src a where a.key > 10 ) subq 
JOIN src tab 
ON (subq.key = tab.key and subq.key > 20 and subq.value = tab.value) 
where tab.value < 200;

SELECT sum(hash(subq.key, tab.value)) 
FROM 
(select a.key, a.value from src a where a.key > 10 ) subq 
JOIN src tab 
ON (subq.key = tab.key and subq.key > 20 and subq.value = tab.value) 
where tab.value < 200;