--liquibase formatted sql

-- changeset irshad:1662896745472
CREATE TABLE app_user(
	id int8 NOT NULL GENERATED ALWAYS AS IDENTITY,
	full_name varchar NULL,
	email varchar UNIQUE NULL,
	phone_number varchar UNIQUE  NULL,
	password varchar NULL,
	account_expired boolean NOT NULL,
	account_locked boolean NOT NULL,
	credential_expired boolean NOT NULL,
	enabled boolean NOT NULL,
	created_at date,
	updated_at date,
	primary KEY(id)
);

CREATE TABLE app_role(
    id int8 NOT NULL GENERATED ALWAYS AS IDENTITY,
    name varchar NULL UNIQUE,
    description varchar NULL,
	created_at date,
	updated_at date,
    PRIMARY KEY(id)
);

CREATE TABLE app_permission(
    id int8 NOT NULL GENERATED ALWAYS AS IDENTITY,
    name varchar NULL UNIQUE,
    description varchar NULL,
	created_at date,
	updated_at date,
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
