FROM anapsix/alpine-java

MAINTAINER Selvam Palanimalai <selvam.palanimalai@gmail.com>

WORKDIR /opt/scala/

COPY target/universal/web-app-0.1-SNAPSHOT.zip web-app.zip

RUN unzip web-app.zip && rm web-app-0.1-SNAPSHOT/conf/application.conf

CMD ln -s /opt/cmaps/application.conf web-app-0.1-SNAPSHOT/conf/application.conf && /opt/scala/web-app-0.1-SNAPSHOT/bin/web-app -Dplay.http.secret.key=$HTTP_SECRET -Dplay.crypto.secret=$APP_SECRET
