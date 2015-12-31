CREATE TABLE twitter2weibo(
id INT NOT NULL AUTO_INCREMENT,
name VARCHAR(64) NOT NULL UNIQUE,
password varchar(256),
email varchar(256),
header_img_url varchar(512),
label VARCHAR(512),
description VARCHAR(1024),
weibo_id varchar(256),
weibo_token varchar(256),
weibo_name VARCHAR(64),
weibo_url VARCHAR(256),
twit_name VARCHAR(64),
twit_url VARCHAR(256),
twit_consumer_key varchar(256),
twit_consumer_secret varchar(256),
twit_access_token varchar(256),
twit_access_token_secret varchar(256),
fans_num INT default 0,
tweets_num INT default 0,
update_time DATETIME,
create_time DATETIME
PRIMARY KEY (id)
) ENGINE=InnoDB default charset=utf8;

