FROM anapsix/alpine-java

MAINTAINER Selvam Palanimalai <selvam.palanimalai@gmail.com>


COPY target/universal/auth-app-0.1-SNAPSHOT.zip /opt/scala/auth-app.zip

RUN unzip /opt/scala/auth-app.zip -d /opt/scala/

CMD /opt/scala/auth-app-0.1-SNAPSHOT/bin/auth-app -Dplay.http.secret.key=$HTTP_SECRET -Dplay.crypto.secret=$APP_SECRET
