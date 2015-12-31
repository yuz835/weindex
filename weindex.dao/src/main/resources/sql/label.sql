CREATE TABLE label (
id INT NOT NULL AUTO_INCREMENT,
cn_name VARCHAR(128) NOT NULL UNIQUE,
en_name VARCHAR(128) NOT NULL UNIQUE,
type INT NOT NULL default 0,
description VARCHAR(512),
visible INT default 0,
pos INT NOT NULL default 0,
PRIMARY KEY (id)
) ENGINE=InnoDB default charset=utf8;

