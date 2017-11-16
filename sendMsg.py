# Download the Python helper library from twilio.com/docs/python/install
from twilio.rest import Client
import psycopg2

try:
    conn = psycopg2.connect("dbname='dcdphn5eus188v' user='rgkhdfsnkyghpp' host='ec2-184-72-255-211.compute-1.amazonaws.com' password='5026c8ab8996d52d7d4ce83b251ea77e3d3c63be9646eed1dad6bfdb4e65e59a'")
    #conn = psycopg2.connect("dbname='swnsggul' user='swnsggul' host='baasu.db.elephantsql.com' password='-fMErUr5HCCr8gcwXQDrzMIzvJv7km_c'")
    if conn:
        print "yea boi"
except:
    print "I am unable to connect to the database"



# Your Account Sid and Auth Token from twilio.com/user/account
account_sid = "ACd7913801d15ef5ef46fa55d88ca154bd"
auth_token = "26e7f55521e7f244fc80275c9d368b14"
client = Client(account_sid, auth_token)


client.api.account.messages.create(
    to="+19786973562",
    from_="+18572147448",
    body="hey there bud do me a favor and dont put a sock in it! keep up the good work! - your friendly robust bud!")

# message = client.messages.create(
#         "+19786973562",
#         body="Jenny please?! I love you <3",
#         from_="+18572147448 ",
#         media_url="http://www.example.com/hearts.png")

#print(message.sid)

