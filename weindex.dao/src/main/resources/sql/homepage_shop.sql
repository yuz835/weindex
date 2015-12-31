CREATE TABLE homepage_shop (
id INT NOT NULL AUTO_INCREMENT,
shop_id INT NOT NULL UNIQUE,
creator_id INT NOT NULL,
creator_name VARCHAR(256),
create_time DATETIME,
PRIMARY KEY (id),
FOREIGN KEY (shop_id) REFERENCES shop(id) on delete cascade 
) ENGINE=InnoDB default charset=utf8;