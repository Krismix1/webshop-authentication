#!/bin/bash
# Wait for config server
status=$(curl http://configserver:8080/actuator/health | jq -r '.status')
until [ "$status" = "UP" ]; do
    echo "Waiting for config server"
    sleep 1
    status=$(curl http://configserver:8080/actuator/health | jq -r '.status')
done
# Wait for discovery server
status=$(curl http://discoveryserver:8080/actuator/health | jq -r '.status')
until [ "$status" = "UP" ]; do
    echo "Waiting for discovery server"
    sleep 1
    status=$(curl http://discoveryserver:8080/actuator/health | jq -r '.status')
done
# Wait for gateway server
status=$(curl http://configserver:8080/actuator/health | jq -r '.status')
until [ "$status" = "UP" ]; do
    echo "Waiting for gateway server"
    sleep 1
    status=$(curl http://configserver:8080/actuator/health | jq -r '.status')
done
echo "Starting auth server"
# Run tomcat
/usr/local/tomcat/bin/catalina.sh run