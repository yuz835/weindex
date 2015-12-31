CREATE TABLE homepage_offer (
id INT NOT NULL AUTO_INCREMENT,
offer_id INT NOT NULL UNIQUE,
creator_id INT NOT NULL,
creator_name VARCHAR(256),
create_time DATETIME,
PRIMARY KEY (id),
FOREIGN KEY (offer_id) REFERENCES offer(id) on delete cascade 
) ENGINE=InnoDB default charset=utf8;