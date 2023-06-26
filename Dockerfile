FROM openjdk:17

MAINTAINER swhors "swhors@naver.com"

ENV API_HOME $HOME/spring-kotlin-server

RUN mkdir -p $API_HOME
ADD ./target/spring-kotlin-server-0.0.1.jar $API_HOME

WORKDIR $API_HOME

EXPOSE 8080

ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

#CMD ["bash", "run.sh", "--spring.profiles.active=local"]
