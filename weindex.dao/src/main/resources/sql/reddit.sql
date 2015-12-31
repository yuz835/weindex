CREATE TABLE reddit (
id INT NOT NULL AUTO_INCREMENT,
user_id INT NOT NULL,
author VARCHAR(512),
title VARCHAR(256) NOT NULL,
type INT default 0,
link_url VARCHAR(256),
description VARCHAR(512),
content MEDIUMTEXT,
label VARCHAR(512),
liked_num INT default 0,
fav_num INT default 0,
visit_num INT NOT NULL default 0,
comment_num INT NOT NULL default 0,
create_time DATETIME,
update_time DATETIME,
PRIMARY KEY (id),
FOREIGN KEY (user_id) REFERENCES user(id) on delete cascade 
) ENGINE=InnoDB default charset=utf8;
