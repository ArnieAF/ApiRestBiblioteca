-- AUTORES
INSERT INTO Autor (nombre, nacionalidad) VALUES ('Gabriel García Márquez', 'Colombiana');
INSERT INTO Autor (nombre, nacionalidad) VALUES ('Isabel Allende', 'Chilena');
INSERT INTO Autor (nombre, nacionalidad) VALUES ('Jorge Luis Borges', 'Argentina');
INSERT INTO Autor (nombre, nacionalidad) VALUES ('Mario Vargas Llosa', 'Peruana');
INSERT INTO Autor (nombre, nacionalidad) VALUES ('J.K. Rowling', 'Britanica');

-- USUARIOS
INSERT INTO Usuario (nombre, email, password, role, fecha_registro) VALUES ('Arnie Adriano', 'arnie@example.com', 'password123', 'ADMIN', '2026-01-10T09:00:00');
INSERT INTO Usuario (nombre, email, password, role, fecha_registro) VALUES ('Ana Torres', 'ana@example.com', 'password123', 'USER', '2026-02-15T10:30:00');
INSERT INTO Usuario (nombre, email, password, role, fecha_registro) VALUES ('Luis Fernández', 'luis@example.com', 'password123', 'USER', '2026-03-20T14:00:00');
-- LIBROS
INSERT INTO Libro (titulo, genero, fecha_publicacion, disponible, id_autor) VALUES ('Cien años de soledad', 'FICTION', '1967-05-30T00:00:00', 1, 1);
INSERT INTO Libro (titulo, genero, fecha_publicacion, disponible, id_autor) VALUES ('El amor en los tiempos del cólera', 'ROMANCE', '1985-01-01T00:00:00', 1, 1);
INSERT INTO Libro (titulo, genero, fecha_publicacion, disponible, id_autor) VALUES ('La casa de los espíritus', 'FICTION', '1982-01-01T00:00:00', 1, 2);
INSERT INTO Libro (titulo, genero, fecha_publicacion, disponible, id_autor) VALUES ('Ficciones', 'MYSTERY', '1944-01-01T00:00:00', 1, 3);
INSERT INTO Libro (titulo, genero, fecha_publicacion, disponible, id_autor) VALUES ('El Aleph', 'FANTASY', '1949-01-01T00:00:00', 0, 3);
INSERT INTO Libro (titulo, genero, fecha_publicacion, disponible, id_autor) VALUES ('La ciudad y los perros', 'FICTION', '1963-01-01T00:00:00', 1, 4);
INSERT INTO Libro (titulo, genero, fecha_publicacion, disponible, id_autor) VALUES ('Harry Potter y la piedra filosofal', 'FANTASY', '1997-06-26T00:00:00', 1, 5);

-- RESERVAS
INSERT INTO Reserva (fecha_reserva, fecha_expiracion, id_libro, id_usuario) VALUES ('2026-06-01T10:00:00', '2026-06-15T10:00:00', 1, 1);
INSERT INTO Reserva (fecha_reserva, fecha_expiracion, id_libro, id_usuario) VALUES ('2026-06-05T11:00:00', '2026-06-19T11:00:00', 3, 2);
INSERT INTO Reserva (fecha_reserva, fecha_expiracion, id_libro, id_usuario) VALUES ('2026-06-10T09:00:00', '2026-06-24T09:00:00', 5, 3);
INSERT INTO Reserva (fecha_reserva, fecha_expiracion, id_libro, id_usuario) VALUES ('2026-07-01T15:00:00', '2026-07-15T15:00:00', 7, 1);