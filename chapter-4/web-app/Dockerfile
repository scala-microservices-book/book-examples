FROM anapsix/alpine-java

MAINTAINER Selvam Palanimalai <selvam.palanimalai@gmail.com>


COPY target/universal/web-app-0.1-SNAPSHOT.zip /opt/scala/web-app.zip

RUN unzip /opt/scala/web-app.zip -d /opt/scala/

CMD /opt/scala/web-app-0.1-SNAPSHOT/bin/web-app -Dplay.http.secret.key=$HTTP_SECRET -Dplay.crypto.secret=$APP_SECRET
