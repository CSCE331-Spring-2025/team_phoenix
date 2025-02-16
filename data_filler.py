import random
from datetime import datetime, timedelta

def populate_order_items(total_orders=36000, num_items=16):
    return 

# TODO: add populate_order_items call into populate_orders
def populate_orders(file_name, sales_target=750000, orders_per_day=100, min_drinks=1, max_drinks=6, avg_drink_price=6):
    with open(file_name, 'a') as file:

        total_sales = 0
        current_date = datetime.today()
        days = 0

        while (total_sales < sales_target):
            daily_sales = 0

            for _ in range(orders_per_day):

                drinks = random.randint(min_drinks,max_drinks)
                order_total = drinks*avg_drink_price
                total_sales += order_total
                daily_sales += order_total

                timestamp = (current_date.replace(hour=23, minute=59) - timedelta(hours=random.randint(0, 14), minutes=random.randint(0, 59))).strftime('%Y-%m-%d %H:%M:%S')
                file.write(f"INSERT INTO orders (employee_id, order_total, time_placed) VALUES ({random.randint(1, 6)}, {order_total}, '{timestamp}');\n")
                # for _ in range(drinks):
                #     populate_order_items()

            days += 1
            current_date -= timedelta(days=1) 
    return days

# delete data from orders table in database b4 adding again

# # paramters
# total_sales_target = 750_000 
# average_drink_price = 6
# min_drinks = 1
# max_drinks = 5
# orders_per_day = 100

# total_sales = 0
# days = 0
# current_date = datetime.today() # start from today

# while total_sales < total_sales_target: # loop until hit sales target
#     daily_sales = 0

#     with open("populateorders.sql", "a") as f:
#         for  in range(orders_per_day):
#             num_drinks = random.randint(min_drinks, max_drinks)
#             total_cost = num_drinks * average_drink_price
#             total_sales += total_cost
#             daily_sales += total_cost

#             timestamp = (current_date.replace(hour=23, minute=59) - timedelta(
#                 hours=random.randint(0, 14), minutes=random.randint(0, 59))
#             ).strftime('%Y-%m-%d %H:%M:%S')

#             f.write(f"INSERT INTO orders (employee_id, total_cost, time_placed) VALUES (
#                 {random.randint(1, 6)}, {total_cost}, '{timestamp}');\n")

#     days += 1
#     current_date -= timedelta(days=1) 

# print(f"SQL file 'populate_orders.sql' generated successfully with {days} days of data.")
