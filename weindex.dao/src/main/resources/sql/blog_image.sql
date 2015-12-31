CREATE TABLE blog_image (
id INT NOT NULL AUTO_INCREMENT,
name VARCHAR(256) NOT NULL,
description VARCHAR(512),
type varchar(10),
img MEDIUMBLOB,
PRIMARY KEY (id)
) ENGINE=InnoDB default charset=utf8;