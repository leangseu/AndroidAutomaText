from crontab import CronTab
 
my_cron = CronTab(user='Alex Cushing')
job = my_cron.new(command='python check.py')
job.minute.every(1)
 
my_cron.write()