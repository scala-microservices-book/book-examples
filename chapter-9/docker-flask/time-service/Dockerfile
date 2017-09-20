From jfloff/alpine-python
 
COPY time_server.py server.py
ENV FLASK_APP server.py
EXPOSE 4000
RUN pip install flask
CMD flask run --host=0.0.0.0 --port=4000

