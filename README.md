# TCP/UDP Server and Client

The TCP Server and Client are two Java programs that enable communication between a server and a client using the TCP/IP protocol. The TCP Server listens for client connections on a specified port, while the TCP Client connects to the server's IP address and port. The server and client exchange data through TCP sockets, allowing for reliable and ordered transmission of messages.

## TCPServer

The TCPServer program represents the server-side of the TCP communication. It creates a server socket and listens for incoming client connections on a specified port. Once a client connects, the server can send and receive messages from the client.


### Code compilation in one go

1. Compile
   ```
   javac Server/*.java
   javac Client/*.java
   ```

2.  Execute

   ```
   java Server/StartTCPServer 9001
   java Server/StartUDPServer 8001

   java Client/StartClient localhost 9001 tcp
   java Client/StartClient localhost 8001 udp
   ```
-------

# Docker Setup

We have also done the docker setup for the project implementation.

## DocKer Installation

Make sure you have docker running on your local computer.

### Usage

To use the UDPServer, follow these steps:

1. Build the docker images for Client, TCP server and UDP server by running the below script:

   ```
   ./build_server_client.sh
   ```

2. To Run the TCP Server, run the below script:

   ```
   ./run_server_tcp.sh
   ```

3. To Run the UDP Server, run the below script:

    ```
   ./run_server_udp.sh
   ```

4. To Run the Unified Client, run the below script:

    ```
   ./run_client.sh <protocol>
   ```
   Protocol will be either `tcp` or `udp`

#Screenshots TCP

## TCP Docker Compilation & Prepopulate

![TCP Prepopulate & perform operations 10 PUT, 5 GET, 5 DELETE](Screenshots/DockerCompilation/SS1.png)

![TCP Prepopulate & perform operations 10 PUT, 5 GET, 5 DELETE](Screenshots/DockerCompilation/SS2.png)

![TCP Prepopulate & perform operations 10 PUT, 5 GET, 5 DELETE](Screenshots/DockerCompilation/SS3.png)

![TCP Prepopulate & perform operations 10 PUT, 5 GET, 5 DELETE](Screenshots/DockerCompilation/SS4.png)

## TCP PUT CLIENT
![TCP PUT](Screenshots/DockerCompilation/TCP-CLIENT-PUT.png)

## TCP PUT SERVER
![TCP PUT](Screenshots/DockerCompilation/TCP-SERVER-PUT.png)


## TCP GET CLIENT
![TCP GET](Screenshots/DockerCompilation/TCP-CLIENT-PUT.png)

## TCP GET SERVER
![TCP GET](Screenshots/DockerCompilation/TCP-SERVER-PUT.png)

## TCP DELETE CLIENT
![TCP DELETE](Screenshots/DockerCompilation/TCP-CLIENT-DELETE.png)

## TCP DELETE SERVER
![TCP DELETE](Screenshots/DockerCompilation/TCP-SERVER-DELETE.png)

## TCP GET-ALL CLIENT
![TCP GET-ALL](Screenshots/DockerCompilation/TCP-CLIENT-GET-ALL.png)

## TCP GET-ALL SERVER
![TCP GET-ALL](Screenshots/DockerCompilation/TCP-SERVER-GET-ALL.png)

## TCP DELETE-ALL CLIENT
![TCP DELETE-ALL](Screenshots/DockerCompilation/TCP-SERVER-DELETE-ALL.png)

## TCP DELETE-ALL SERVER
![TCP DELETE-ALL](Screenshots/DockerCompilation/TCP-CLIENT-DELETE-ALL.png)

#Screenshots UDP

## UDP Docker Compilation & Prepopulate

![UDP Docker Compilation & Prepopulate](Screenshots/DockerCompilation/SS6.png)

![UDP Docker Compilation & Prepopulate](Screenshots/DockerCompilation/SS6.png)

## UDP PUT, GET, DELETE, GET-ALL CLIENT
![UDP PUT, GET, DELETE, GET-ALL CLIENT](Screenshots/DockerCompilation/UDP-CLIENT-PUT-GET-DELETE-GETALL.png)

## UDP PUT, GET, DELETE, GET-ALL SERVER
![UDP PUT, GET, DELETE, GET-ALL CLIENT](Screenshots/DockerCompilation/UDP-SERVER-PUT-GET-DELETE-GETALL.png)

## UDP DELETE-ALL CLIENT
![UDP DELETE-ALL CLIENT](Screenshots/DockerCompilation/UDP-CLIENT-DELETE-ALL.png)

## UDP DELETE-ALL SERVER
![UDP DELETE-ALL SERVER](Screenshots/DockerCompilation/UDP-SERVER-PUT-GET-DELETE-GETALL.png)
