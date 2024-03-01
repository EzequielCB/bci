CREATE SEQUENCE users_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE phone_seq START WITH 1 INCREMENT BY 1;

CREATE TABLE users (
  id INT NOT NULL,
   uuid RAW(16) NOT NULL,
   token VARCHAR(255),
   name VARCHAR(255) NOT NULL,
   email VARCHAR(255) NOT NULL,
   password VARCHAR(255) NOT NULL,
   created_at TIMESTAMP,
   updated_at TIMESTAMP,
   last_login TIMESTAMP,
   is_active BOOLEAN,
   CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE users ADD CONSTRAINT uc_users_name UNIQUE (name);

ALTER TABLE users ADD CONSTRAINT uc_users_uuid UNIQUE (uuid);


CREATE TABLE phone (
  id INT NOT NULL,
   number VARCHAR(255) NOT NULL,
   city_code VARCHAR(255) NOT NULL,
   country_code VARCHAR(255) NOT NULL,
   user_id INT,
   CONSTRAINT pk_phone PRIMARY KEY (id)
);

ALTER TABLE phone ADD CONSTRAINT uc_phone_number UNIQUE (number);

ALTER TABLE phone ADD CONSTRAINT FK_PHONE_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);
