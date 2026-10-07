
-- Comercio inicial para la evaluación

INSERT INTO comercios (nombre, categoria, direccion, abierto)
SELECT
    'Restaurante Kinal',
    'RESTAURANTE',
    'Ciudad de Guatemala',
    true
WHERE NOT EXISTS (
    SELECT 1
    FROM comercios
    WHERE nombre = 'Restaurante Kinal'
);
