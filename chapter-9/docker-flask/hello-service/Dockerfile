From jfloff/alpine-python:2.7
 
COPY server.py server.py
ENV FLASK_APP server.py
EXPOSE 5000
RUN pip install flask
RUN pip install requests
CMD flask run --host=0.0.0.0

