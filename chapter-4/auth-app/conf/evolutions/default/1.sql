# --- !Ups

create table users (email VARCHAR NOT NULL PRIMARY KEY,passwdHash VARCHAR NOT NULL, creationTime BIGINT NOT NULL );

create table tokens(key VARCHAR NOT NULL  PRIMARY KEY , token VARCHAR NOT NULL UNIQUE , validTill BIGINT NOT NULL)

# --- !Downs

drop table users;