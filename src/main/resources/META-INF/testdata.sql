# first install and start a mysql server on your machine
# then:
# $ mysql -h localhost -uroot -p < src/main/resources/META-INF/testdata.sql

TRUNCATE TABLE todo.TodoItem;
INSERT INTO todo.TodoItem VALUES (23, 'Item 23');