FROM bitnami/wildfly:latest
EXPOSE 8080
COPY target/Lab_2_SA-1.0-SNAPSHOT.war /opt/bitnami/wildfly/standalone/deployments