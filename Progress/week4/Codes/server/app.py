import psycopg2
from flask import Flask
from flask import request, Response
#from dateutil import parser
from datetime import datetime
import calendar

#set FLASK_APP=server.py
#python -m flask run

app = Flask(__name__)

try:
    conn = psycopg2.connect("dbname='dcdphn5eus188v' user='rgkhdfsnkyghpp' host='ec2-184-72-255-211.compute-1.amazonaws.com' password='5026c8ab8996d52d7d4ce83b251ea77e3d3c63be9646eed1dad6bfdb4e65e59a'")
    if conn:
        print("yea boi")
except:
    print("I am unable to connect to the database")

@app.route('/', methods = ['get'])
def index():
    return "hello"


@app.route('/saveText', methods = ['POST'])
def saveText():
    phoneNumber = request.form['phoneNumber']
    message = request.form['message']
    date = request.form['date']
    time = request.form['time']

    roughDateTime = str(date + ' ' + time)


    covertedDateTime = datetime.strptime(roughDateTime, '%m/%d/%Y %I:%M %p')
    epochDateTime = calendar.timegm(covertedDateTime.timetuple())

    textArray = [] 
    textArray.append(str(phoneNumber))
    textArray.append(str(message))
    textArray.append(str(epochDateTime))
    saveTextoDb(textArray)

    if saveTextoDb(textArray):
        return Response("{'success':'true'}", status=200, mimetype='application/json')
    else:
        return Response("{'success':'false'}", status=500, mimetype='application/json')

    return "yes"


def saveTextoDb(textArray):
    cur = conn.cursor()
    try:
        cur.execute("INSERT INTO text_repo(to_number, text_body, time_to_send) VALUES (%s, %s, %s)", (textArray[0], textArray[1], textArray[2]))
        conn.commit()
        return True
    except OSError as err:
        print("insert text failed :[", err)
        return False