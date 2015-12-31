CREATE TABLE user_session (
id INT NOT NULL AUTO_INCREMENT,
user_id INT NOT NULL,
token VARCHAR(512) NOT NULL,
create_time BIGINT NOT NULL,
expiry_time BIGINT NOT NULL,
valid INT default 1,
PRIMARY KEY (id),
FOREIGN KEY (user_id) REFERENCES user(id) on delete cascade
) ENGINE=InnoDB default charset=utf8;