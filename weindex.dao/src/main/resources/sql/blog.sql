CREATE TABLE blog (
id INT NOT NULL AUTO_INCREMENT,
shop_id INT NOT NULL,
author VARCHAR(512),
title VARCHAR(256) NOT NULL,
summary VARCHAR(512),
label VARCHAR(512),
shop_label VARCHAR(512),
content MEDIUMTEXT,
visible INT default 0,
visit_num INT NOT NULL default 0,
comment_num INT NOT NULL default 0,
pos INT NOT NULL default 0,
create_time DATETIME,
update_time DATETIME,
PRIMARY KEY (id),
FOREIGN KEY (shop_id) REFERENCES shop(id) on delete cascade 
) ENGINE=InnoDB default charset=utf8;