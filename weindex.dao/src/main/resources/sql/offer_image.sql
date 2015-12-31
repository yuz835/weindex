CREATE TABLE offer_image (
id INT NOT NULL AUTO_INCREMENT,
offer_id INT NOT NULL,
name VARCHAR(256) NOT NULL,
description VARCHAR(512),
type varchar(10),
img MEDIUMBLOB,
offer_logo INT NOT NULL default 0,
PRIMARY KEY (id),
FOREIGN KEY (offer_id) REFERENCES offer(id) on delete cascade 
) ENGINE=InnoDB default charset=utf8;