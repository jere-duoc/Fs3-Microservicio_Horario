
USE horarios;


CREATE TABLE turnos (
    id_turno BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    nombre_turno VARCHAR(50) NOT NULL,
    hora_inicio_default TIME NOT NULL,
    hora_fin_default TIME NOT NULL
);


CREATE TABLE horarios (
    id_horario BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    hora_entrada TIME NOT NULL,
    hora_salida TIME NOT NULL,
    horas_extra INT DEFAULT 0,
    id_turno BIGINT UNSIGNED,
    id_usuario BIGINT UNSIGNED,
    id_sala BIGINT UNSIGNED,
    FOREIGN KEY (id_turno) REFERENCES turnos(id_turno)
);

INSERT INTO turnos (nombre_turno, hora_inicio_default, hora_fin_default) VALUES
('Diurno', '08:00:00', '16:00:00'),
('Vespertino', '16:00:00', '00:00:00'),
('Nocturno', '00:00:00', '08:00:00');
