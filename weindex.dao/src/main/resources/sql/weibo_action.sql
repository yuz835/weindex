CREATE TABLE weibo_action (
id INT NOT NULL AUTO_INCREMENT,
weibo_user_id varchar(256),
weibo_id varchar(256),
weibo_comment_id varchar(256),
action_type INT default 0,
response_type INT default 0,
action_status INT default 0,
response_content VARCHAR(512),
image_name varchar(256),
received_time DATETIME ,
response_time  DATETIME,
PRIMARY KEY (id)
) ENGINE=InnoDB default charset=utf8;
