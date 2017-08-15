FROM anapsix/alpine-java

MAINTAINER Selvam Palanimalai <selvam.palanimalai@gmail.com>


COPY target/universal/so-app-0.1-SNAPSHOT.zip /opt/scala/so-app.zip

RUN unzip /opt/scala/so-app.zip -d /opt/scala/

CMD /opt/scala/so-app-0.1-SNAPSHOT/bin/so-app -Dplay.http.secret.key=$HTTP_SECRET -Dplay.crypto.secret=$APP_SECRET
