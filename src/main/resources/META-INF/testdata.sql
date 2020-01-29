-- first install and start a mysql server on your machine
-- then:
-- $ mysql -h localhost -uroot -p < src/main/resources/META-INF/testdata.sql

TRUNCATE TABLE TodoItem;
INSERT INTO TodoItem VALUES (23, 'Item 23');