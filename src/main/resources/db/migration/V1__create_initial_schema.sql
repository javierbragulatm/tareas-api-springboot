CREATE TABLE usuarios (
 	id BIGINT AUTO_INCREMENT PRIMARY KEY,
 	username VARCHAR(255) NOT NULL,
 	password VARCHAR(255) NOT NULL,
 	CONSTRAINT uk_usuarios_username UNIQUE (username) 
); 
    
CREATE TABLE tarea ( 
	id INT AUTO_INCREMENT PRIMARY KEY, 
	titulo VARCHAR(100) NOT NULL, 
	completada BOOLEAN NOT NULL, 
	usuario_id BIGINT NOT NULL, 
	CONSTRAINT fk_tarea_usuario 
		FOREIGN KEY (usuario_id) 
		REFERENCES usuarios(id) 
);