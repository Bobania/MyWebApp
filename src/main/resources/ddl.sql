DROP TABLE clients CASCADE;
DROP TABLE products CASCADE;
DROP TABLE orders CASCADE;
DROP TABLE order_clients;


CREATE TABLE clients (
         id SERIAL PRIMARY KEY,
         name VARCHAR(100) NOT NULL,
         surname VARCHAR(100) NOT NULL,
         phone VARCHAR(15) NOT NULL
);

CREATE TABLE products(
         id SERIAL PRIMARY KEY,
         title VARCHAR(100) NOT NULL,
         price DECIMAL(10, 2)
);

CREATE TABLE orders (
        id SERIAL PRIMARY KEY,
        client_id BIGINT,
        product_id BIGINT,
        FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE,
        FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

CREATE TABLE order_clients (
       order_id BIGINT,
       client_id BIGINT,
       FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
       FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE
);