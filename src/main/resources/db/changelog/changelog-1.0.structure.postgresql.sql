--liquibase formatted sql

-- changeset irshad:1662896745472-1
CREATE TABLE app_user(
	id int8 NOT NULL GENERATED ALWAYS AS IDENTITY,
	identifier varchar NOT NULL,
	full_name varchar NULL,
	email varchar UNIQUE NULL,
	phone_number varchar UNIQUE  NULL,
	password varchar NULL,
	account_expired boolean NOT NULL,
	account_locked boolean NOT NULL,
	credential_expired boolean NOT NULL,
	enabled boolean NOT NULL,
	created_by varchar,
	created_at timestamptz ,
	updated_by varchar,
	updated_at timestamptz ,
	primary KEY(id)
);

CREATE TABLE app_role(
    id int8 NOT NULL GENERATED ALWAYS AS IDENTITY,
    name varchar NULL UNIQUE,
    description varchar NULL,
	created_by varchar,
	created_at timestamptz ,
	updated_by varchar,
	updated_at timestamptz ,
    PRIMARY KEY(id)
);

CREATE TABLE app_permission(
    id int8 NOT NULL GENERATED ALWAYS AS IDENTITY,
    name varchar NULL UNIQUE,
    description varchar NULL,
	created_by varchar,
	created_at timestamptz ,
	updated_by varchar,
	updated_at timestamptz ,
    PRIMARY KEY(id)
);

CREATE TABLE app_user_role(
    user_id int8 NOT NULL,
    role_id int8 NOT NULL,
    PRIMARY KEY(user_id, role_id),
    constraint fk_user foreign key(user_id) references app_user(id),
    constraint fk_role foreign key(role_id) references app_role(id)
);

CREATE TABLE app_role_permission(
    role_id int8 NOT NULL,
    permission_id int8 NOT NULL,
    PRIMARY KEY(role_id, permission_id),
    constraint fk_role foreign key(role_id) references app_role(id),
    constraint fk_permission foreign key(permission_id) references app_permission(id)
);

-- changeset irshad:1664014849939-1
CREATE TABLE app_token (
	id int8 NOT NULL GENERATED ALWAYS AS IDENTITY,
	jit varchar NOT null unique,
	token_type char(1) NULL,
	active bool NOT NULL,
	hits int4 NOT NULL,
	created_by varchar,
	created_at timestamptz ,
	updated_by varchar,
	updated_at timestamptz ,
	PRIMARY KEY (id)
);
