-- first install and start a mysql server on your machine
-- then:
-- $ mysql -h localhost -uroot -p < src/main/resources/META-INF/createdb.sql

DROP USER IF EXISTS 'todo-web'@'localhost';
DROP DATABASE IF EXISTS todo;

CREATE USER 'todo-web'@'localhost' IDENTIFIED BY '1234';
CREATE DATABASE todo DEFAULT CHARACTER SET utf8;
GRANT INSERT,SELECT,UPDATE,DELETE,DROP,CREATE ON todo.* TO 'todo-web'@'localhost';
FLUSH PRIVILEGES;