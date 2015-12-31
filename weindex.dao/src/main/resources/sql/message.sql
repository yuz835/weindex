CREATE TABLE message (
id INT NOT NULL AUTO_INCREMENT,
type INT NOT NULL,
title VARCHAR(256),
content VARCHAR(512),
source VARCHAR(128),
source_id INT,
dest VARCHAR(128),
dest_id INT,
readed INT DEFAULT 0,
create_time DATETIME,
PRIMARY KEY (id)
) ENGINE=InnoDB default charset=utf8;