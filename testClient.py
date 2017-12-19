import urllib2
choice = ""
while (choice != "exit"):
    print "http://127.0.0.1:5000/",
    choice = raw_input()
    response = urllib2.urlopen('http://127.0.0.1:5000/' + choice)
    html = response.read()
    print html
