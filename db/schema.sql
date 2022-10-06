# create lib database
create database lib;

# create book table
CREATE TABLE book
(id BIGINT NOT NULL,
 title VARCHAR(255), author_name VARCHAR(255),
 book_status VARCHAR(255), publisher VARCHAR(255),
 last_page_number INTEGER, total_page INTEGER, image_id BIGINT,
 category_id BIGINT, user_id BIGINT, PRIMARY KEY (id));

CREATE TABLE category (id BIGINT NOT NULL, name VARCHAR(255), PRIMARY KEY (id));

CREATE TABLE images (id BIGINT NOT NULL, image_url VARCHAR(255), PRIMARY KEY (id));