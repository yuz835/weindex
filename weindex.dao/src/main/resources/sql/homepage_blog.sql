CREATE TABLE homepage_blog (
id INT NOT NULL AUTO_INCREMENT,
blog_id INT NOT NULL UNIQUE,
creator_id INT NOT NULL,
creator_name VARCHAR(256),
create_time DATETIME,
PRIMARY KEY (id),
FOREIGN KEY (blog_id) REFERENCES blog(id) on delete cascade 
) ENGINE=InnoDB default charset=utf8;