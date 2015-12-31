CREATE TABLE weibo_task (
id INT NOT NULL AUTO_INCREMENT,
user_id INT NOT NULL,
type INT NOT NULL,
task_id INT NOT NULL,
content VARCHAR(512),
task_time DATETIME,
status INT default 0,
create_time DATETIME,
PRIMARY KEY (id),
FOREIGN KEY (user_id) REFERENCES user(id) on delete cascade 
) ENGINE=InnoDB default charset=utf8;