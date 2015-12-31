CREATE TABLE comment (
id INT NOT NULL AUTO_INCREMENT,
offer_id INT NOT NULL,
title VARCHAR(256),
content VARCHAR(512),
creater VARCHAR(128),
creater_id INT,
create_time DATETIME,
visible INT default 0,
PRIMARY KEY (id),
FOREIGN KEY (offer_id) REFERENCES offer(id) on delete cascade 
) ENGINE=InnoDB default charset=utf8;