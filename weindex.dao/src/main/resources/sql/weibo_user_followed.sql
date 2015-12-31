CREATE TABLE weibo_user_followed (
id INT NOT NULL AUTO_INCREMENT,
weibo_user_id varchar(256),
screen_name varchar(256),
first_followed_index INT default 0,
latest_followed_index INT default 0,
created DATETIME ,
PRIMARY KEY (id)
) ENGINE=InnoDB default charset=utf8;
