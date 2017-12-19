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
        send +="</p>"
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

@app.route('/insertparking/<userfirstname>/<userlastname>/<latitude>/<longitude>/<address>/<hours>/<costPerHour>/', methods=['GET', 'POST'])
def insert_parking(userfirstname, userlastname, latitude, longitude, address, hours, costPerHour):
    cursor.execute('''SELECT lastname FROM users WHERE firstname = \'''' + userfirstname + '''\'''')
    ans = cursor.fetchall()[0][0]
    print ans, userlastname
    if (userlastname == ans):
        query = '''INSERT into parkings values (?,?,?,?,?,?,?,?)'''
        data = (userfirstname, latitude, longitude, address, hours, costPerHour, 0, 0)
        it = cursor.execute(query, data)
        for i in it:
            print i + ", "
        if (it != None):
            db.commit()
            return "Success"
    return "Error in insertion"

@app.route('/parkings/list')
def parking_page():
    # show the user profile for that user
    query = '''SELECT * FROM parkings LIMIT 6'''
    cursor.execute(query)
    ans = cursor.fetchall()
    send = ""
    for j in ans:
        for i in j: send += str(i) + ","
        send += "\n"
    print send
    return send

@app.route('/user/<username>')
def show_user_profile(username):
    # show the user profile for that user
    query = '''SELECT * FROM users WHERE firstname = \''''+username+'''\''''
    cursor.execute(query)
    ans = cursor.fetchall()
    send = "The user's stats: \n"
    for i in ans[0]: send += str(i) + ", "
    print send
    return send

if __name__ == '__main__':
    db = sqlite3.connect('example.db')
    cursor = db.cursor()
    atexit.register(db.close)
    #cursor.execute('''DROP TABLE parkings''')
    #cursor.execute('''CREATE TABLE users (firstname text, lastname text, email text, hasParking integer)''')
    #cursor.execute('''CREATE TABLE parkings (user text, latitude integer, longitude integer, address text unique,\
    #hours text, costPerHour integer, ratings integer, numberOfRaters integer)''')
    app.run()