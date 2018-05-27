#!/bin/bash
# Wait for discovery server
status=$(curl http://$DISCOVERY_IP:$DISCOVERY_PORT | jq -r '.error')
until [ "$status" = "unauthorized" ]; do
    echo "Waiting for discovery server"
    sleep 1
    status=$(curl http://$DISCOVERY_IP:$DISCOVERY_PORT | jq -r '.error')
done
# Wait for gateway server
status=$(curl http://$GATEWAY_IP:$GATEWAY_PORT | jq -r '.error')
until [ "$status" = "unauthorized" ]; do
    echo "Waiting for gateway server"
    sleep 1
    status=$(curl http://$GATEWAY_IP:$GATEWAY_PORT | jq -r '.error')
done
echo "Server is running"
# Run tomcat
/usr/local/tomcat/bin/catalina.sh run
