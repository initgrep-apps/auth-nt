--liquibase formatted sql

--changeset irshad:1660935387352
--CREATE TABLE app_user(
--    id int8 NOT NULL GENERATED ALWAYS AS IDENTITY,
--    username varchar NULL,
--    password varchar NULL,
--	created_at date,
--	updated_at date,
--    primary KEY(id)
--);

--changeset irshad:1662896745470
CREATE TABLE app_user(
	id int8 NOT NULL GENERATED ALWAYS AS IDENTITY,
	full_name varchar NULL,
	email varchar UNIQUE NULL,
	phone_number varchar UNIQUE  NULL,
	password varchar NULL,
	created_at date,
	updated_at date,
	primary KEY(id)
);
CREATE TABLE address (
	id int8 NOT NULL GENERATED ALWAYS AS IDENTITY,
	user_id int8 ,
	house varchar NULL,
	street varchar NULL,
	city varchar NULL,
	state varchar NULL,
	country varchar NULL,
	pincode varchar null,
    created_at date,
    updated_at date,
	primary key(id),
	constraint fk_app_user
		foreign key(user_id)
			references app_user(id)
);
