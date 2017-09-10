# --- !Ups

create table users (email VARCHAR NOT NULL PRIMARY KEY,passwdHash VARCHAR NOT NULL, creationTime BIGINT NOT NULL );

create table tokens(key VARCHAR NOT NULL  PRIMARY KEY , token VARCHAR NOT NULL UNIQUE , validTill BIGINT NOT NULL);

--- a user is by default created. password is: test
insert into users  (email, passwdHash, creationTime) values ('test@test.com','$2a$11$7M4wUE4VYQBDEHd4eQUbpuiOzl4tB5gZnmQ3t06LNkCbBlwjtRukO',1505070611328);
insert into tokens (key, token, validTill) values ('test@test.com','123-456-789-123',2505070611328);

# --- !Downs

drop table users;
drop table tokens;