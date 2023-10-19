# Step 1 compile java code to war
FROM maven:3.9.4-eclipse-temurin-17-alpine as build

WORKDIR /home/app

COPY pom.xml .

COPY src src

RUN mvn -B package --file pom.xml

#Step 2 copy war to wildfly server
FROM quay.io/wildfly/wildfly:29.0.1.Final-jdk17 as final

COPY --from=build /home/app/target/system-architecture-lab-two-1.0.war /opt/jboss/wildfly/standalone/deployments/

RUN /opt/jboss/wildfly/bin/add-user.sh admin Admin#70365 --silent

EXPOSE 8080 9990

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]