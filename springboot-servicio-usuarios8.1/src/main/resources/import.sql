INSERT INTO `usuarios` (username, password, enabled, nombre, apellido, email) VALUES('RRRR','$2a$10$qRSKOc8mpa4ranRejXtKpu8wxsvPE5r/2LVZvkMZymwGN11WzJMUS',1,'RRRR','IIII','RRRR@IIII.COM');
INSERT INTO `usuarios` (username, password, enabled, nombre, apellido, email) VALUES('ZZZZ','$2a$10$W8fiIkd4tcfyAUi.FLktcux6VfqBCGWQ863rL6Cr3z0176.xurHky',1,'ZZZZ','YYYY','ZZZZ@YYYY.COM');

INSERT INTO `roles` (nombre) VALUES ('ROLE_USER');
INSERT INTO `roles` (nombre) VALUES ('ROLE_ADMIN');

INSERT INTO `usuarios_roles` (usuario_id, role_id) VALUES (1,1);
INSERT INTO `usuarios_roles` (usuario_id, role_id) VALUES (2,2);
INSERT INTO `usuarios_roles` (usuario_id, role_id) VALUES (2,1);