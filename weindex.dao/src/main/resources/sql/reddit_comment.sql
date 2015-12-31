CREATE TABLE reddit_comment (
id INT NOT NULL AUTO_INCREMENT,
reddit_id INT NOT NULL,
content VARCHAR(512),
creator_id INT NOT NULL,
creator_name varchar(256),
trace_comment_id INT,
level INT default 0,
liked_num INT default 0,
create_time DATETIME,
PRIMARY KEY (id),
FOREIGN KEY (reddit_id) REFERENCES reddit(id) on delete cascade 
) ENGINE=InnoDB default charset=utf8;
