CREATE TABLE offer (
id INT NOT NULL AUTO_INCREMENT,
shop_id INT NOT NULL,
name VARCHAR(256) NOT NULL,
description VARCHAR(512),
label VARCHAR(512),
shop_label VARCHAR(512),
img_url VARCHAR(256),
price VARCHAR(128),
quality INT NOT NULL default 100,
type INT default 0,
visible INT default 0,
visit_num INT NOT NULL default 0,
comment_num INT NOT NULL default 0,
pos INT NOT NULL default 0,
update_time DATETIME,
PRIMARY KEY (id),
FOREIGN KEY (shop_id) REFERENCES shop(id) on delete cascade 
) ENGINE=InnoDB default charset=utf8;