FROM bellsoft/liberica-openjdk-alpine-musl:21 AS client-build
COPY . src/
WORKDIR /src
RUN javac Client/StartClient.java

FROM bellsoft/liberica-openjdk-alpine-musl:21 AS server-udp-build
COPY . src/
WORKDIR /src
RUN javac Server/StartUDPServer.java
CMD ["java", "Server/StartUDPServer", "9000"]

FROM bellsoft/liberica-openjdk-alpine-musl:21 AS server-tcp-build
COPY . src/
WORKDIR /src
RUN javac Server/StartUDPServer.java
CMD ["java", "Server/StartTCPServer", "8000"]