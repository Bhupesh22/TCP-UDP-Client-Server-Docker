PROJECT_NETWORK='tcp_udp'
TCP_SERVER_IMAGE='tcp-server'
UDP_SERVER_IMAGE='udp-server'
TCP_SERVER_CONTAINER='my-tcp-server'
UDP_SERVER_CONTAINER='my-udp-server'
CLIENT_IMAGE='app-client'
CLIENT_CONTAINER='my_client'
TCP_CLIENT_CONTAINER='my_tcp_client'
UDP_CLIENT_CONTAINER='my_udp_client'

# clean up existing resources, if any
echo "----------Cleaning up existing resources----------"
docker container stop $TCP_SERVER_CONTAINER 2> /dev/null && docker container rm $TCP_SERVER_CONTAINER 2> /dev/null
docker container stop $UDP_SERVER_CONTAINER 2> /dev/null && docker container rm $UDP_SERVER_CONTAINER 2> /dev/null
docker container stop $TCP_CLIENT_CONTAINER 2> /dev/null && docker container rm $TCP_CLIENT_CONTAINER 2> /dev/null
docker container stop $UDP_CLIENT_CONTAINER 2> /dev/null && docker container rm $UDP_CLIENT_CONTAINER 2> /dev/null
docker network rm $PROJECT_NETWORK 2> /dev/null

# only cleanup
if [ "$1" == "cleanup-only" ]
then
  exit
fi

# create a custom virtual network
echo "----------creating a virtual network----------"
docker network create $PROJECT_NETWORK

# build the images from Dockerfile
echo "----------Building images----------"
docker build -t $CLIENT_IMAGE --target client-build .
docker build -t $TCP_SERVER_IMAGE --target server-tcp-build .
docker build -t $UDP_SERVER_IMAGE --target server-udp-build .