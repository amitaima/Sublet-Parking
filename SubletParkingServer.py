import atexit
from flask import Flask
import sqlite3
app = Flask('__main__')

@app.route('/hello')
def respond():
    return "hi"

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
    query = '''INSERT into users values (?,?,?,?)'''
    data = (firstname, lastname, mail, int(hasparking))
    it = cursor.execute(query, data)
    for i in it:
        print i + ", "
    if (it == None):
        return "Error in insertion"
    else:
        db.commit()
        return "Success"

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
    #cursor.execute('''CREATE TABLE users (firstname text, lastname text, email text, hasParking integer)''')
    app.run()


#curl -d "firstname=hallel&lastname=segel&role=author" -X POST 127.0.0.1:5000/people
#cd C:\Program Files\curl-7.56.1\I386
#cd C:\Program Files\MongoDB\Server\3.4\bin
#mongod