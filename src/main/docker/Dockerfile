FROM registry.cn-beijing.aliyuncs.com/billon379/jdk-alpine:8
ENV TZ=Asia/Shanghai
ENV SPRING_PROFILES_ACTIVE=""
VOLUME /tmp
ADD member-rest.jar member-rest.jar
ENTRYPOINT ["java","-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}","-jar","/member-rest.jar"]