DROP VIEW testView;
CREATE VIEW testView as SELECT * FROM srcpart;
DESCRIBE FORMATTED testView;

ALTER VIEW testView AS SELECT value FROM src WHERE key=86;
DESCRIBE FORMATTED testView;

ALTER VIEW testView AS
SELECT * FROM src
WHERE key > 80 AND key < 100
ORDER BY key, value
LIMIT 10;
DESCRIBE FORMATTED testView;
