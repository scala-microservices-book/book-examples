from flask import Flask
from time import gmtime, strftime
app = Flask(__name__)

@app.route('/')
def get_time():
    return strftime("%Y-%m-%d %H:%M:%S", gmtime())
