
/*
CREATE TABLE name (
    id SERIAL PRIMARY KEY,
    other_id INT REFERENCES other(id),
    object_name TEXT NOT NULL,
    num INT DEFAULT 0,
    price NUMERIC(10,2),
    is_manager BOOLEAN DEFAULT FALSE,
    time_placed TIMESTAMP DEFAULT NOW()
);
*/

CREATE TABLE employees (
    id SERIAL PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    is_manager BOOLEAN DEFAULT FALSE,
    manager_PIN CHAR(4) CHECK (manager_PIN ~ '^\d{4}$')
);
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    employee_id INT REFERENCES employees(id),
    total_cost NUMERIC(10,2) NOT NULL,
    time_placed TIMESTAMP DEFAULT NOW()
);
CREATE TABLE menu_items (
    id SERIAL PRIMARY KEY,
    item_name TEXT NOT NULL,
    price NUMERIC(10,2) NOT NULL,
    times_ordered INT DEFAULT 0,
);
CREATE TABLE items_in_order (
    junction_id SERIAL PRIMARY KEY,
    order_id INT REFERENCES orders(id),
    menu_id INT REFERENCES menu_items(id),
);
CREATE TABLE suppliers (
    id SERIAL PRIMARY KEY,
    supplier_name TEXT NOT NULL,
    supplier_name VARCHAR(15) NOT NULL,
    delivery_day CHAR(3)
);
CREATE TABLE inventory (
    id SERIAL PRIMARY KEY,
    supplier_id INT REFERENCES suppliers(id),
    item_name TEXT NOT NULL,
    quantity INT DEFAULT 0 CHECK (quantity >= 0),
);
CREATE TABLE ingredients_in_item (
    junction_id SERIAL PRIMARY KEY,
    menu_id INT REFERENCES menu_items(id),
    inventory_id INT REFERENCES inventory(id),
);
