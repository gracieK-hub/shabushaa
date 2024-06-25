import pymysql

# 假设您已经从安全的来源获取了数据库连接信息
host = "192.168.66.23"
port = 3306
user = "root"
password = "Yi93*yL!dslk7654"  # 从环境变量或配置文件中获取
database = "wangc"

try:
    with pymysql.connect(
        host=host,
        port=port,
        user=user,
        password=password,
        database=database
    ) as db:
        with db.cursor() as cursor:
            # 执行查询
            cursor.execute("select first_name from employee")
            # 获取查询结果
            results = cursor
            # 打印结果
            for row in results:
                name = row[0]
                print(name)
except Exception as e:
    print(f"An error occurred: {e}")


# # 员工数据列表，每个元素是一个包含所有字段的元组
# employee_data = [
#     ('wang1', 'chen1', 20, 'M', 2000),
#     ('wang2', 'chen2', 30, 'F', 3000),
# ]
#
# # 使用参数化查询以提高代码安全性
# sql = """insert into employee(first_name, last_name, age, sex, income)
# values (%s, %s, %s, %s, %s)"""
#
# # 使用 try-except 来捕获并处理特定的异常
# try:
#     # 使用 with 语句自动管理资源
#     with pymysql.connect(
#         host=host,
#         port=port,
#         user=user,
#         password=password,
#         database=database
#     ) as db:
#         with db.cursor() as cursor:
#             # 执行批量插入
#             cursor.executemany(sql, employee_data)
#             # 提交更改
#             db.commit()
# except Exception as e:
#     db.rollback()




# cursor = db.cursor()
# cursor.execute("drop table if exists employee")
# sql = """create table employee (
# first_name char(20) not null,
# last_name char(20),
# age int,
# sex char(1),
# income float)"""
#
# cursor.execute(sql)
#
# db.close()

# import mysql.connector
#
# mydb = mysql.connector.connect(
#     host="192.168.66.23",
#     port="3306",
#     user="root",
#     passwd="Yi93*yL!dslk7654"
# )
#
# mycursor = mydb.cursor()
#
# mycursor.execute("select * from wangc.git")
#
# for e in mycursor:
#     print(e)
