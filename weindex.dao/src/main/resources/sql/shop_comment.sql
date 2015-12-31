CREATE TABLE shop_comment (
id INT NOT NULL AUTO_INCREMENT,
shop_id INT NOT NULL,
title VARCHAR(256) NOT NULL,
content VARCHAR(512),
visible INT default 0,
creator_id INT NOT NULL,
creator_name varchar(256),
trace_comment_id INT,
create_time DATETIME,
update_time DATETIME,
PRIMARY KEY (id),
FOREIGN KEY (shop_id) REFERENCES shop(id) on delete cascade 
) ENGINE=InnoDB default charset=utf8;