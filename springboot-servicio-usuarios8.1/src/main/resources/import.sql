INSERT INTO `usuarios` (username, password, enabled, nombre, apellido, email) VALUES('RRRR','12345',1,'RRRR','IIII','RRRR@IIII.COM');
INSERT INTO `usuarios` (username, password, enabled, nombre, apellido, email) VALUES('ZZZZ','12345',1,'ZZZZ','YYYY','ZZZZ@YYYY.COM');

INSERT INTO `roles` (nombre) VALUES ('ROLE_USER');
INSERT INTO `roles` (nombre) VALUES ('ROLE_ADMIN');

INSERT INTO `usuarios_roles` (usuario_id, role_id) VALUES (1,1);
INSERT INTO `usuarios_roles` (usuario_id, role_id) VALUES (2,2);
INSERT INTO `usuarios_roles` (usuario_id, role_id) VALUES (2,1);