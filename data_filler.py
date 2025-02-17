import random
from datetime import datetime, timedelta

'''
Cannot populate both tables at once, psql enforces a foreign key constraint.
Script creates full order history. Affects orders, items_in_order tables.
'''


def populate_order_items(file_name, order_id, num_drinks, num_items=16):
    # adds items for each order, make sure drink count matches
    with open(file_name, 'a') as file:

        # randomly select drinks from menu
        chosen_items = random.sample(range(1, num_items + 1), num_drinks)
        
        # write SQL insert statement for each chosen item
        for menu_id in chosen_items:
            file.write(f"INSERT INTO items_in_order (order_id, menu_id) VALUES ({order_id}, {menu_id});\n")


def populate_orders(file_name, sales_target=750000, orders_per_day=100, min_drinks=1, max_drinks=6, avg_drink_price=6):
    with open(file_name, 'a') as file:

        total_sales = 0
        current_date = datetime.today()
        days = 0
        order_id = 1  # track order ID

        # loop until sales target is reached AND atleast 40 weeks have passed
        while total_sales <= sales_target and days <= 275:
            daily_sales = 0
            
            # two peak days with roughly double the orders
            if current_date.month == 8 and current_date.day == 14:
                daily_orders = int(orders_per_day * 2)
            elif current_date.month == 12 and current_date.day == 4:
                daily_orders = int(orders_per_day * 1.8)
            else:
                daily_orders = orders_per_day

            # generate orders for the day
            for _ in range(daily_orders):
                drinks = random.randint(min_drinks, max_drinks) 
                order_total = drinks * avg_drink_price
                total_sales += order_total
                daily_sales += order_total
                
                # random timestamp for the order, between 10 am and 10 pm
                timestamp = (current_date.replace(hour=23, minute=59) - timedelta(
                    hours=random.randint(2, 14), minutes=random.randint(0, 59))
                ).strftime('%Y-%m-%d %H:%M:%S')
                
                # write SQL insert statement for the order
                file.write(f"INSERT INTO orders (employee_id, total_cost, time_placed) VALUES ({random.randint(1, 6)}, {order_total}, '{timestamp}');\n")
                
                # populate items within the order
                populate_order_items('testItems.sql', order_id, drinks, num_items=16)
                
                order_id += 1  # increment order ID

            days += 1
            current_date -= timedelta(days=1)
    
    print(days)

populate_orders('testOrders.sql', sales_target=750000, orders_per_day=100, min_drinks=1, max_drinks=6, avg_drink_price=6)