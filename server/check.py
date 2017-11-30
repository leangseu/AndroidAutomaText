# Download the Python helper library from twilio.com/docs/python/install
from twilio.rest import Client
import psycopg2
import calendar
import time

try:
    conn = psycopg2.connect("dbname='dcdphn5eus188v' user='rgkhdfsnkyghpp' host='ec2-184-72-255-211.compute-1.amazonaws.com' password='5026c8ab8996d52d7d4ce83b251ea77e3d3c63be9646eed1dad6bfdb4e65e59a'")
    #conn = psycopg2.connect("dbname='swnsggul' user='swnsggul' host='baasu.db.elephantsql.com' password='-fMErUr5HCCr8gcwXQDrzMIzvJv7km_c'")
    if conn:
        print("yea boi")
except:
    print("I am unable to connect to the database")



# Your Account Sid and Auth Token from twilio.com/user/account
account_sid = "ACd7913801d15ef5ef46fa55d88ca154bd"
auth_token = "26e7f55521e7f244fc80275c9d368b14"
client = Client(account_sid, auth_token)

cur = conn.cursor()
cur.execute("""SELECT to_number, text_body, id FROM text_repo WHERE time_to_send >= %s""", [calendar.timegm(time.gmtime())])
phoneNums = cur.fetchall()

for num in phoneNums:
    print(num)
    mynum = num[0]
    body = num[1]
    _id = num[2]
    mynum = 1+mynum.replace("-", "") if mynum[0] == '1' else mynum.replace("-", "")
    client.api.account.messages.create(
    to=mynum,
    from_="+18572147448",
    body=body)
    cur.execute("""DELETE FROM text_repo WHERE id = %s""", [_id])
    conn.commit()
