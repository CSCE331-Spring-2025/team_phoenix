from numpy import random
import math
from datetime import datetime, timedelta

'''
Cannot populate both tables at once, psql enforces a foreign key constraint.
Script creates full order history. Affects orders, items_in_order tables.
'''
price_of_drink = [6.49, 7.29, 7.79, 6.99, 7.29, 7.29, 6.79, 6.49, 6.79, 6.49, 6.29, 7.29, 6.79, 6.29, 7.29, 6.29]
def get_drink_price(menu_id):
    return price_of_drink[menu_id-1]
def orders_in_day(avg):
    return math.ceil(random.normal(loc=avg, scale=25))
def number_of_drinks():
    return math.ceil(math.fabs(random.normal(loc=3, scale=2)))


def populate_order_items(file_name, order_id, num_drinks, num_items=16):
    # adds items for each order, make sure drink count matches
    with open(file_name, 'a') as file:

        # randomly select drinks from menu
        chosen_items = random.choice(range(1, num_items + 1), num_drinks)

        order_total = 0 
        
        # write SQL insert statement for each chosen item
        for menu_id in chosen_items:
            order_total += get_drink_price(menu_id)
            file.write(f"INSERT INTO items_in_order (order_id, menu_id) VALUES ({order_id}, {menu_id});\n")
    return order_total # to track varying drink prices


def populate_orders(orders_file_name='populateOrders.sql',items_file_name='populateItems.sql', sales_target=750000, orders_per_day=150, days_target=275):
    with open(orders_file_name, 'a') as orders_file:

        total_sales = 0
        current_date = datetime.today()
        days = 0
        order_id = 1  # track order ID

        # loop until sales target is reached AND atleast 40 weeks have passed
        while total_sales <= sales_target or days <= days_target:
            daily_sales = 0
            
            # two peak days with roughly double the orders
            # random number of orders daily with std dev of 25
            if current_date.month == 8 and current_date.day == 14:
                daily_orders = orders_in_day(orders_per_day * 2)
            elif current_date.month == 12 and current_date.day == 4:
                daily_orders = orders_in_day(orders_per_day * 1.8)
            else:
                daily_orders = orders_in_day(orders_per_day)

            # generate orders for the day
            for _ in range(daily_orders):
                drinks = int(number_of_drinks()) # avg 3 with std dev of 2
                # order_total = drinks * avg_drink_price 
                # ^^^ won't work with varying drink values
                
                # random timestamp for the order, between 10 am and 10 pm
                timestamp = (current_date.replace(hour=23, minute=59) - timedelta(
                    hours=random.randint(2, 14), minutes=random.randint(0, 59))
                ).strftime('%Y-%m-%d %H:%M:%S')
                
                # write SQL insert statement to populate junction table items_in_order
                order_total = populate_order_items(items_file_name, order_id, drinks, num_items=16)
                daily_sales += order_total

                # write SQL insert statement for the order
                orders_file.write(f"INSERT INTO orders (employee_id, total_cost, time_placed) VALUES ({random.randint(1, 6)}, {order_total}, '{timestamp}');\n")
                
                order_id += 1  # increment order ID

            days += 1
            current_date -= timedelta(days=1)
            total_sales += daily_sales

    print(days)

# delete all parameters to generate full data files 
populate_orders(orders_file_name='randOrdersTest.sql', items_file_name='randItemsTest.sql', sales_target=2000, orders_per_day=75, days_target=5)
