import threading
import time

exit_flag = 0

def print_time(thread_name, delay, counter):
    while counter:
        if exit_flag:
            thread_name.exit()
        time.sleep(delay)
        print(f"{thread_name}:{time.ctime(time.time())}")
        counter -= 1

class my_thread(threading.Thread):
    def __init__(self, threadid, name, delay):
        threading.Thread.__init__(self)
        self.threadid = threadid
        self.name = name
        self.delay = delay
    def run(self):
        print(f"线程启动: {self.name}")
        print_time(self.name, self.delay, 5)
        print(f"线程结束: {self.name}")

thread1 = my_thread(1, "Thread-1", 1)
thread2 = my_thread(2, "Thread-2", 2)

thread1.start()
thread2.start()
thread1.join()
thread2.join()


# def print_number():
#     for i in range(5):
#         time.sleep(1)
#         print(i)
#
# thread = threading.Thread(target=print_number)
#
# thread.start()
#
# thread.join()

# import _thread
# import time
#
# def print_time(thread_name, delay):
#     count = 0
#     while count < 5:
#         time.sleep(delay)
#         count += 1
#         print(thread_name, ":", time.ctime(time.time()))
#
# try:
#     _thread.start_new_thread(print_time, ("Thread-1", 2))
#     _thread.start_new_thread(print_time, ("Thread-2", 4))
# except Exception as e:
#     print("Error: unable to start thread", e)
#
# while 1:
#     pass