/*
This file contains a list of SQL queries that can be run as an input file to verify the low-level design and interactions in our database. 
*/

-- Special Query #1: "Weekly Sales History"
SELECT 
    EXTRACT(WEEK FROM time_placed) AS weeks, 
    COUNT(*) AS total_orders
FROM orders
GROUP BY weeks ORDER BY weeks;

-- Special Query #2: "Realistic Sales History"
SELECT 
    EXTRACT(HOUR FROM time_placed) AS daily_hour, 
    COUNT(id) AS total_orders, 
    SUM(total_cost) AS total_sales
FROM orders
GROUP BY daily_hour
ORDER BY daily_hour;

-- Special Query #3: "Peak Sales Day"
SELECT 
    DATE(time_placed) AS order_date, 
    SUM(total_cost) AS total_sales
FROM orders
GROUP BY order_date
ORDER BY total_sales DESC
LIMIT 10;

-- Total Sales Amount (750,000 sales required)
SELECT sum(total_cost) AS total_sales FROM orders;

-- # of Days (39 weeks or 273 days required)
SELECT count(DISTINCT DATE(time_placed)) AS total_days FROM orders;

-- # of Menu Items (16 menu items required)
SELECT count(id) AS total_items FROM menu_items;

-- Orders by each Employee
SELECT menu_id, COUNT(*) AS times_ordered
FROM items_in_order
GROUP BY menu_id
ORDER BY times_ordered DESC;

-- Low Inventory Items
SELECT item_name, quantity FROM inventory WHERE quantity < 100;

-- Stocked Items
SELECT item_name, quantity FROM inventory WHERE quantity > 100;

-- Supplier Contributions
SELECT s.supplier_name, COUNT(i.id) AS num_items_supplied
FROM suppliers s
JOIN inventory i ON s.id = i.supplier_id
GROUP BY s.supplier_name;

-- View of Ingredients in an Item
SELECT i.item_name AS ingredients
FROM ingredients_in_item ii
JOIN inventory i ON ii.inventory_id = i.id
JOIN menu_items m ON ii.menu_id = m.id
WHERE m.item_name = 'Coffee Milk Tea';

-- See the Manager!!
SELECT first_name, last_name, manager_PIN
FROM employees
WHERE is_manager = TRUE;


-- Most Expensive Order Ever
SELECT id, total_cost FROM orders ORDER BY total_cost DESC 
LIMIT 1;



/*
random helpful queries
*/

-- resorts table by primary key if you made a bunch of changes
-- (PSQL doesnt do this automatically)
CLUSTER employees USING employees_pkey;


-------- MILES --------

-- Veiw all triggers
SELECT * FROM triggers;

/*  Items in orders joined with the orders table 
    Shows orders in descnding order by order_id  */
SELECT * FROM order_summary;

-- See all items in a specific order
SELECT * FROM order_summary WHERE order_id = 300;

SELECT * FROM daily_sales;
