from flask import Flask
import socket
import requests
app = Flask(__name__)

@app.route('/')
def hello_world():
    curr_time = requests.get('http://scalamicroservices_time:4000').content
    print curr_time
    hostname=socket.gethostname()
    return 'Hello, World! Yours truly, '+ hostname + ', Time: ' + curr_time

