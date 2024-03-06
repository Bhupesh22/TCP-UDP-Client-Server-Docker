CLIENT_IMAGE='app-client'
PROJECT_NETWORK='tcp_udp'
SERVER_IMAGE='tcp-server'
SERVER_CONTAINER='my-tcp-server'
# run the image and open the required ports
echo "----------Running sever app----------"
docker run -d --rm -p 8000:8000/tcp --name $SERVER_CONTAINER --network $PROJECT_NETWORK $SERVER_IMAGE

echo "----------watching logs from server----------"
docker logs $SERVER_CONTAINER -f