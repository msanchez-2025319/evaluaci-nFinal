
-- Usuarios iniciales para la evaluación
-- Contraseña de prueba: Prueba123

INSERT INTO usuarios (nombre, direccion, telefono, email, password, rol)
SELECT
    'Administrador Kinal',
    'Guatemala',
    '55550001',
    'admin@kinal.test',
    '$2a$10$FE9KXtIpfX9OlcbDqnpDLuCqLl7juMaPenEOfEM4vJfx2V/DUZf9u',
    'ADMIN'
WHERE NOT EXISTS (
    SELECT 1 FROM usuarios WHERE email = 'admin@kinal.test'
);

INSERT INTO usuarios (nombre, direccion, telefono, email, password, rol)
SELECT
    'Repartidor Kinal',
    'Guatemala',
    '55550002',
    'repartidor@kinal.test',
    '$2a$10$FE9KXtIpfX9OlcbDqnpDLuCqLl7juMaPenEOfEM4vJfx2V/DUZf9u',
    'REPARTIDOR'
WHERE NOT EXISTS (
    SELECT 1 FROM usuarios WHERE email = 'repartidor@kinal.test'
);

INSERT INTO usuarios (nombre, direccion, telefono, email, password, rol)
SELECT
    'Cliente Kinal',
    'Guatemala',
    '55550003',
    'cliente@kinal.test',
    '$2a$10$FE9KXtIpfX9OlcbDqnpDLuCqLl7juMaPenEOfEM4vJfx2V/DUZf9u',
    'CLIENTE'
WHERE NOT EXISTS (
    SELECT 1 FROM usuarios WHERE email = 'cliente@kinal.test'
);
