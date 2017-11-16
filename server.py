import psycopg2
from flask import Flask
from flask import request


#set FLASK_APP=server.py
#python -m flask run

app = Flask(__name__)

try:
    conn = psycopg2.connect("dbname='dcdphn5eus188v' user='rgkhdfsnkyghpp' host='ec2-184-72-255-211.compute-1.amazonaws.com' password='5026c8ab8996d52d7d4ce83b251ea77e3d3c63be9646eed1dad6bfdb4e65e59a'")
    #conn = psycopg2.connect("dbname='swnsggul' user='swnsggul' host='baasu.db.elephantsql.com' password='-fMErUr5HCCr8gcwXQDrzMIzvJv7km_c'")
    if conn:
        print "yea boi"
except:
    print "I am unable to connect to the database"


@app.route('/saveText/', methods = ['POST'])
def saveText():
    print "test", request.values
    #phoneNumber = request.args.get('phoneNumber')
    phoneNumber = request.form['phoneNumber']
    message = request.form['message']
    date = request.form['date']
    time = request.form['time']
    #message = request.args.get('message')
    #date = request.args.get('date')
    #time = request.args.get('time')

    print "phoneNumber", phoneNumber
    print "message", message
    print "date", date
    print "time", time

    return "yes"

