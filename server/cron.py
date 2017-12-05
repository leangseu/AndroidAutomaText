import schedule
import time
import os
dir_path = os.path.dirname(os.path.realpath(__file__))
def job():
    os.system('py '+dir_path+'/check.py')

schedule.every(5).seconds.do(job)

while 1:
    schedule.run_pending()
    time.sleep(1)