--liquibase formatted sql

-- changeset irshad:1662896745472-2
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

INSERT INTO app_role ( name, description, created_at, updated_at) VALUES( 'user', 'a normal user of the application', now(), now());
INSERT INTO app_role ( name, description, created_at, updated_at) VALUES( 'admin', 'Admin of the application', now(), now());
INSERT INTO app_role ( name, description, created_at, updated_at) VALUES( 'reviewer', 'can read the info of all app', now(),now());

-- changeset irshad:1662896745472-3
INSERT INTO app_permission (name, description, created_at, updated_at) VALUES( 'account:read', 'user can read his own information', now(), now());
INSERT INTO app_permission (name, description, created_at, updated_at) VALUES( 'account:write', 'user can update his own information', now(), now());
INSERT INTO app_permission (name, description, created_at, updated_at) VALUES( 'account:delete', 'can delete a user account', now(), now());
INSERT INTO app_permission (name, description, created_at, updated_at) VALUES( 'account:readAll', 'can read info of all users', now(), now());
INSERT INTO app_permission (name, description, created_at, updated_at) VALUES( 'account:addAuthority', 'can add a role to a user ', now(), now());
INSERT INTO app_permission (name, description, created_at, updated_at) VALUES( 'account:removeAuthority', 'can remove a role to a user ', now(), now());
INSERT INTO app_permission (name, description, created_at, updated_at) VALUES( 'auth:read', 'can get its own auth info', now(), now());
INSERT INTO app_permission (name, description, created_at, updated_at) VALUES( 'authority:read', 'can read a role or permission info', now(), now());
INSERT INTO app_permission (name, description, created_at, updated_at) VALUES( 'authority:write', 'can write a role or permission info', now(), now());
INSERT INTO app_permission (name, description, created_at, updated_at) VALUES( 'authority:delete', 'can delete a role or permission info', now(), now());
INSERT INTO app_permission (name, description, created_at, updated_at) VALUES( 'authority:readAll', 'can read all roles and permission', now(), now());

-- changeset irshad:1662896745472-4
INSERT INTO app_role_permission (role_id, permission_id)
VALUES((select id from app_role  where name ='user'),(select id from app_permission where name = 'account:read'));

INSERT INTO app_role_permission (role_id, permission_id)
VALUES((select id from app_role  where name ='user'),(select id from app_permission where name = 'account:write'));

INSERT INTO app_role_permission (role_id, permission_id)
VALUES((select id from app_role  where name ='user'),(select id from app_permission where name = 'auth:read'));

-- changeset irshad:1662896745472-5
INSERT INTO app_role_permission (role_id, permission_id)
VALUES((select id from app_role  where name ='admin'),(select id from app_permission where name = 'auth:read'));

INSERT INTO app_role_permission (role_id, permission_id)
VALUES((select id from app_role  where name ='admin'),(select id from app_permission where name = 'account:read'));

INSERT INTO app_role_permission (role_id, permission_id)
VALUES((select id from app_role  where name ='admin'),(select id from app_permission where name = 'account:write'));

INSERT INTO app_role_permission (role_id, permission_id)
VALUES((select id from app_role  where name ='admin'),(select id from app_permission where name = 'account:delete'));

INSERT INTO app_role_permission (role_id, permission_id)
VALUES((select id from app_role  where name ='admin'),(select id from app_permission where name = 'account:readAll'));

INSERT INTO app_role_permission (role_id, permission_id)
VALUES((select id from app_role  where name ='admin'),(select id from app_permission where name = 'account:addAuthority'));

INSERT INTO app_role_permission (role_id, permission_id)
VALUES((select id from app_role  where name ='admin'),(select id from app_permission where name = 'account:removeAuthority'));

INSERT INTO app_role_permission (role_id, permission_id)
VALUES((select id from app_role  where name ='admin'),(select id from app_permission where name = 'authority:read'));

INSERT INTO app_role_permission (role_id, permission_id)
VALUES((select id from app_role  where name ='admin'),(select id from app_permission where name = 'authority:write'));

INSERT INTO app_role_permission (role_id, permission_id)
VALUES((select id from app_role  where name ='admin'),(select id from app_permission where name = 'authority:delete'));

INSERT INTO app_role_permission (role_id, permission_id)
VALUES((select id from app_role  where name ='admin'),(select id from app_permission where name = 'authority:readAll'));

-- changeset irshad:1662896745472-6
INSERT INTO app_role_permission (role_id, permission_id)
VALUES((select id from app_role  where name ='reviewer'),(select id from app_permission where name = 'auth:read'));

INSERT INTO app_role_permission (role_id, permission_id)
VALUES((select id from app_role  where name ='reviewer'),(select id from app_permission where name = 'account:read'));

INSERT INTO app_role_permission (role_id, permission_id)
VALUES((select id from app_role  where name ='reviewer'),(select id from app_permission where name = 'account:write'));

INSERT INTO app_role_permission (role_id, permission_id)
VALUES((select id from app_role  where name ='reviewer'),(select id from app_permission where name = 'account:readAll'));

INSERT INTO app_role_permission (role_id, permission_id)
VALUES((select id from app_role  where name ='reviewer'),(select id from app_permission where name = 'account:addAuthority'));

INSERT INTO app_role_permission (role_id, permission_id)
VALUES((select id from app_role  where name ='reviewer'),(select id from app_permission where name = 'account:removeAuthority'));

INSERT INTO app_role_permission (role_id, permission_id)
VALUES((select id from app_role  where name ='reviewer'),(select id from app_permission where name = 'authority:read'));

INSERT INTO app_role_permission (role_id, permission_id)
VALUES((select id from app_role  where name ='reviewer'),(select id from app_permission where name = 'authority:readAll'));



