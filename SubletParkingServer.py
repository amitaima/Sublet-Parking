import atexit
from flask import Flask
import sqlite3
import re

app = Flask('__main__')


@app.route('/deleteuser/<firstname>/<lastname>')
def delete_user(firstname, lastname):
    cursor.execute('''SELECT lastname FROM users WHERE firstname = \'''' + firstname + '''\'''')
    ans = cursor.fetchall()[0][0]
    print ans, lastname
    if (lastname == ans):
        query = '''DELETE FROM users WHERE firstname = \'''' + firstname + '''\''''
        it = cursor.execute(query)
        if (it != None):
            db.commit()
            return "Success"
    return "Error in insertion"


@app.route('/users')
def get_users():
    # show the user profile for that user
    query = '''SELECT * FROM users'''
    cursor.execute(query)
    send = ""
    for user in cursor.fetchall():
        send += "<p> user stats: "
        for i in user: send += str(i) + ", "
        send += "</p>"
    print send
    return send


@app.route('/insertuser/<firstname>/<lastname>/<mail>/<hasparking>', methods=['GET', 'POST'])
def insert_user(firstname, lastname, mail, hasparking):
    if re.match("[^@]+@[^@]+\.[^@]+", mail):
        query = '''INSERT into users values (?,?,?,?)'''
        data = (firstname, lastname, mail, int(hasparking))
        it = cursor.execute(query, data)
        for i in it:
            print i + ", "
        if (it != None):
            db.commit()
            return "Success"
    return "Error in insertion"


@app.route('/insertparking/<userid>/<latitude>/<longitude>/<address>/<hours>/<costPerHour>/',
           methods=['GET', 'POST'])
def insert_parking(userid, latitude, longitude, address, hours, costPerHour):
    query = '''INSERT into parkings values (?,?,?,?,?,?,?,?)'''
    data = (userid, latitude, longitude, address, hours, costPerHour, 0, 0)
    it = cursor.execute(query, data)
    for i in it:
        print i + ", "
    if (it != None):
        db.commit()
        return "Success"
    return "Error in insertion"


@app.route('/parkings/page')
def parking_page():
    # show the user profile for that user
    query = '''SELECT * FROM parkings LIMIT 6'''
    cursor.execute(query)
    ans = cursor.fetchall()
    send = "[\n"
    for j in ans:
        if j != ans[0]: send += ","
        send += buildParkingJSON(j) + "\n"
    send += "]"
    print send
    return send


@app.route('/user/<username>')
def show_user_profile(username):
    # show the user profile for that user
    query = '''SELECT * FROM users WHERE firstname = \'''' + username + '''\''''
    cursor.execute(query)
    ans = cursor.fetchall()
    send = "The user's stats: \n"
    for i in ans[0]: send += str(i) + ", "
    print send
    return send

def buildParkingJSON(parkingFromSQL):
    return """{
        "userId":"""+ str(parkingFromSQL[0]) +""",
        "longitude":"""+ str(parkingFromSQL[1]) +""",
        "latitude":"""+ str(parkingFromSQL[2]) +""",
        "address":"""+ parkingFromSQL[3] +""",
        "hours":"""+ parkingFromSQL[4] +""",
        "costPerHour":"""+ str(parkingFromSQL[5]) +""",
        "rating":"""+ str(parkingFromSQL[6]) +""",
        "numberOfRaters":"""+ str(parkingFromSQL[7]) +"""
        }"""

if __name__ == '__main__':
    db = sqlite3.connect('example.db')
    cursor = db.cursor()
    atexit.register(db.close)
    cursor.execute('''UPDATE parkings SET latitude=3''')
    cursor.execute('''UPDATE parkings SET longitude=3''')
    #cursor.execute('''DROP TABLE parkings''')
    # cursor.execute('''CREATE TABLE users (firstname text, lastname text, email text, hasParking integer)''')
    #cursor.execute('''CREATE TABLE parkings (userId integer, latitude float, longitude float, address text unique,\
     #hours text, costPerHour integer, ratings integer, numberOfRaters integer)''')
    app.run(host="0.0.0.0")
