CREATE TABLE shop_label (
id INT NOT NULL AUTO_INCREMENT,
shop_id INT NOT NULL,
cn_name VARCHAR(128) NOT NULL UNIQUE,
en_name VARCHAR(128) NOT NULL UNIQUE,
type INT NOT NULL default 0,
description VARCHAR(512),
visible INT default 0,
PRIMARY KEY (id),
FOREIGN KEY (shop_id) REFERENCES shop(id) on delete cascade 
) ENGINE=InnoDB default charset=utf8;

