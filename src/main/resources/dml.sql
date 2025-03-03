INSERT INTO clients (name, surname, phone) VALUES
       ('Ivan', 'Ivanov', '123456789'),
       ('Jane', 'Smith', '987654321'),
       ('Alice', 'Johnson', '555666777');
INSERT INTO products (title, price) VALUES
        ('Bread', 55.50),
        ('Cake', 999.00),
        ('Donut', 100),
        ('Cookie', 240.00),
        ('Croissant', 120.00);
INSERT INTO orders (product_id) VALUES
        (1), -- Хлеб
        (2), -- Торт
        (3); -- Печенья

INSERT INTO order_clients (order_id, client_id) VALUES
        (1, 1),
        (1, 2),
        (2, 1),
        (3, 3);