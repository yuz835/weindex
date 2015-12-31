CREATE TABLE disliked_reddit (
id INT NOT NULL AUTO_INCREMENT,
reddit_id INT NOT NULL,
user_id INT NOT NULL,
public INT NOT NULL default 0,
create_time DATETIME,
PRIMARY KEY (id),
FOREIGN KEY (reddit_id) REFERENCES reddit(id) on delete cascade,
FOREIGN KEY (user_id) REFERENCES user(id) on delete cascade 
) ENGINE=InnoDB default charset=utf8;
