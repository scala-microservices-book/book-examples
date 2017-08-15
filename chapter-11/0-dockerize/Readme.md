FROM anapsix/alpine-java

MAINTAINER Selvam Palanimalai 


COPY target/universal/auth-app-0.1-SNAPSHOT.zip /opt/scala/auth-app.zip

RUN unzip /opt/scala/auth-app.zip

CMD "/opt/scala/auth-app/bin/auth-app -Dplay.http.secret.key=abcdefghijk -Dplay.crypto.secret=$APP_SECRET"