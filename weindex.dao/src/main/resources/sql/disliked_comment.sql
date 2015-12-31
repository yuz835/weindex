CREATE TABLE disliked_comment (
id INT NOT NULL AUTO_INCREMENT,
comment_id INT NOT NULL,
user_id INT NOT NULL,
type INT NOT NULL,
public INT NOT NULL default 0,
create_time DATETIME,
PRIMARY KEY (id),
FOREIGN KEY (user_id) REFERENCES user(id) on delete cascade 
) ENGINE=InnoDB default charset=utf8;
