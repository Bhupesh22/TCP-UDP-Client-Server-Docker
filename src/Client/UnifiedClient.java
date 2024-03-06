package Client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

public class UnifiedClient {
    static String key, value, request;
  static Scanner scanner;
  static BufferedReader bufferedReader;

  /**
   * The main start point of the UDPClient program.
   *
   * @param args command line arguments containing server IP and port.
   * @throws IOException if an I/O error occurs.
   */
  public static void main(String[] args) throws IOException {
    if (args.length != 3 || Integer.parseInt(args[1]) > 65535) {
      throw new IllegalArgumentException("Invalid argument! " +
          "Please provide valid IP and Port number and start again");
    }
    InetAddress serverIP = InetAddress.getByName(args[0]);
    int serverPort = Integer.parseInt(args[1]);
    String protocol = args[2];
    if (protocol.toLowerCase().equals("udp"))
      UDPClient(serverIP, serverPort);
    else if (protocol.toLowerCase().equals("tcp"))
      TCPClient(serverIP, serverPort);
    else {
      throw new IllegalArgumentException("Invalid argument! " +
          "Please provide valid protocol tcp or udp and start again");
    }

  }

  /**
   * Establishes a UDP client connection to a server and provides an interactive
   * interface for sending and receiving
   * requests.
   *
   * @param serverIP   The IP address of the server to connect to.
   * @param serverPort The port number of the server to connect to.
   * @throws IOException          If an error occurs during socket creation or
   *                              communication.
   * @throws UnknownHostException If the provided server IP address is invalid.
   */
  private static void UDPClient(InetAddress serverIP, int serverPort) throws IOException {
    scanner = new Scanner(System.in);

    try (DatagramSocket datagramSocket = new DatagramSocket()) {
      datagramSocket.setSoTimeout(10000);
      String start = getTimeStamp();
      System.out.println(start + " Client started");

      prepopulateRequests(serverIP, serverPort);

      while (true) {
        System.out.println("---------------------------------------");
        System.out.print("Operations: \n1. PUT\n2. GET\n3. DELETE\n4. GET-ALL\n5. DELETE-ALL\nChoose operation number: ");
        String operation = scanner.nextLine().trim();
        if (Objects.equals(operation, "1")) {
          getKey();
          getValue();
          request = "PUT " + key + " " + value;
        } else if (Objects.equals(operation, "2")) {
          getKey();
          request = "GET " + key;
        } else if (Objects.equals(operation, "3")) {
          getKey();
          request = "DELETE " + key;
        } else if (Objects.equals(operation, "4")){
          request = "GET-ALL";
        }  else if(Objects.equals(operation, "5")){
          request = "DELETE-ALL";
        }
        else {
          System.out.println("Please choose a valid operation!");
          continue;
        }

        requestLog(request);
        sendRequest(datagramSocket, serverIP, serverPort, request);
      }
    } catch (UnknownHostException | SocketException e) {
      System.out.println(
          "Host or Port unknown error, try again!");
    }
  }

  /**
   * Sends a series of pre-defined PUT, GET, and DELETE requests to the server to
   * initialize the connection and
   * potentially populate data structures.
   *
   * @param port The port number of the server to send the requests to.
   * @throws IOException If an error occurs during socket creation or
   *                     communication.
   */
  private static void prepopulateRequests(InetAddress serverIP, int port) throws IOException {
    DatagramSocket datagramSocket = new DatagramSocket();
    System.out.println("Prepopulating UDP");
    sendRequests("PUT", 1, 5, port, datagramSocket, serverIP);
    sendRequests("GET", 1, 5, port, datagramSocket, serverIP);
    sendRequests("DELETE", 1, 5, port, datagramSocket, serverIP);
    //datagramSocket.close();
  }

  /**
   * Sends a sequence of requests of a specified type to the server.
   *
   * @param requestType    The type of requests to send (PUT, GET, or DELETE).
   * @param start          The starting index for the sequence of requests.
   * @param end            The ending index for the sequence of requests.
   * @param port           The port number of the server to send the requests to.
   * @param datagramSocket The DatagramSocket used for communication.
   * @param serverIP       The IP address of the server to send the requests to.
   * @throws IOException If an error occurs during sending or receiving.
   */
  private static void sendRequests(String requestType, int start, int end, int port, DatagramSocket datagramSocket,
      InetAddress serverIP) throws IOException {
    for (int i = start; i < end + 1; i++) {
        String request = "";
        if(requestType == "PUT"){
            request = requestType +" "+i+" "+(i*100);
        } else {
            request = requestType + " " + i;
        }
      sendRequest(datagramSocket, serverIP, port, request);
    }
  }

  /**
   * Sends a single request to the server and receives the response.
   *
   * @param datagramSocket The DatagramSocket used for communication.
   * @param serverIP       The IP address of the server to send the request to.
   * @param port           The port number of the server to send the request to.
   * @param request        The request message to be sent.
   * @throws IOException If an error occurs during sending or receiving.
   */
  private static void sendRequest(DatagramSocket datagramSocket, InetAddress serverIP, int port, String request)
      throws IOException {
    byte[] requestBuffer = request.getBytes();
    if (requestBuffer.length > 65499) {
      System.out.println("Error: Request size exceeds the maximum allowed limit.");
      return;
    }

    long checksum = generateChecksum(requestBuffer);

    byte[] requestData = generateData(requestBuffer, checksum);
    DatagramPacket requestPacket = createDatagramPacket(requestData, serverIP, port);
    datagramSocket.send(requestPacket);

    try {
      byte[] resultBuffer = receiveResponse(datagramSocket);
      String result = new String(resultBuffer);
      responseLog(result);
    } catch (java.net.SocketTimeoutException e) {
      System.out.println("Timeout occurred. The server did not respond within the specified time.");
    }
  }

  /**
   * Creates a DatagramPacket for sending data over a UDP socket.
   *
   * @param buffer   The byte array containing the data to be sent.
   * @param serverIP The IP address of the server to send the datagram to.
   * @param port     The port number of the server to send the datagram to.
   * @return A DatagramPacket object ready for sending.
   */
  private static DatagramPacket createDatagramPacket(byte[] buffer, InetAddress serverIP, int port) {
    return new DatagramPacket(buffer, buffer.length, serverIP, port);
  }

  /**
   * Receives a response from the server over a UDP socket.
   *
   * @param datagramSocket The DatagramSocket used for communication.
   * @return The byte array containing the received response data.
   * @throws IOException If an error occurs during receiving.
   */
  private static byte[] receiveResponse(DatagramSocket datagramSocket) throws IOException {
    byte[] resultBuffer = new byte[512];
    DatagramPacket resultPacket = new DatagramPacket(resultBuffer, resultBuffer.length);
    datagramSocket.receive(resultPacket);
    return resultPacket.getData();
  }

  /**
   * Sends a series of pre-defined PUT, GET, and DELETE requests to the server
   * over a TCP connection to initialize the
   * connection and potentially populate data structures.
   *
   * @param dataOutputStream The DataOutputStream used to send requests to the
   *                         server.
   * @param dataInputStream  The DataInputStream used to receive responses from
   *                         the server.
   * @throws IOException If an error occurs during sending or receiving.
   */
  private static void prepopulateTCPRequests(DataOutputStream dataOutputStream, DataInputStream dataInputStream)
      throws IOException {
    System.out.println("Prepopulating TCP");

    sendAndProcessRequests(dataOutputStream, dataInputStream, "PUT", 1, 10);
    sendAndProcessRequests(dataOutputStream, dataInputStream, "GET", 1, 5);
    sendAndProcessRequests(dataOutputStream, dataInputStream, "DELETE", 1, 5);
  }

  /**
   * Sends a sequence of requests of a specified type to the server over a TCP
   * connection and processes the responses.
   *
   * @param dataOutputStream The DataOutputStream used to send requests to the
   *                         server.
   * @param dataInputStream  The DataInputStream used to receive responses from
   *                         the server.
   * @param requestType      The type of requests to send (PUT, GET, or DELETE).
   * @param start            The starting index for the sequence of requests.
   * @param end              The ending index for the sequence of requests.
   * @throws IOException If an error occurs during sending or receiving.
   */
  private static void sendAndProcessRequests(DataOutputStream dataOutputStream, DataInputStream dataInputStream,
      String requestType, int start, int end) throws IOException {
    for (int i = start; i <= end; i++) {
        if(requestType == "PUT"){
            request = requestType + " " + i +" " + (i*100);
        } else {
            request = requestType + " " + i;
        }
      String response = sendAndReceivePacket(dataOutputStream, dataInputStream, request);

      if (response.startsWith("ERROR")) {
        System.out.println("Received error response from the server: " + response);
      } else {
        responseLog(response);
      }
    }
  }

  /**
   * Sends a request to the server over a TCP connection and receives the
   * response.
   *
   * @param dataOutputStream The DataOutputStream used to send requests to the
   *                         server.
   * @param dataInputStream  The DataInputStream used to receive responses from
   *                         the server.
   * @param request          The request message to be sent.
   * @return The response message received from the server.
   * @throws IOException If an error occurs during sending or receiving.
   */
  private static String sendAndReceivePacket(DataOutputStream dataOutputStream, DataInputStream dataInputStream,
      String request) throws IOException {
    sendPacket(dataOutputStream, request);
    return receivePacket(dataInputStream);
  }

  /**
   * Establishes a TCP client connection to a server and provides an interactive
   * interface for sending and receiving
   * requests over the TCP connection.
   *
   * @param serverIP   The IP address of the server to connect to.
   * @param serverPort The port number of the server to connect to.
   */
  private static void TCPClient(InetAddress serverIP, int serverPort) {
    scanner = new Scanner(System.in);
    try (Socket s = new Socket()) {
      int timeout = 10000;
      s.connect(new InetSocketAddress(serverIP, serverPort), timeout);

      DataInputStream dataInputStream = new DataInputStream(s.getInputStream());
      DataOutputStream dataOutputStream = new DataOutputStream(s.getOutputStream());
      bufferedReader = new BufferedReader(new InputStreamReader(System.in));
      String start = getTimeStamp();
      System.out.println(start + " Client started on port: " + s.getPort());

      prepopulateTCPRequests(dataOutputStream, dataInputStream);

      while (true) {
        System.out.println("---------------------------------------");
        System.out.print("Operations: \n1. PUT\n2. GET\n3. DELETE \n4.GET-ALL \n5.DELETE-ALL\nChoose operation number: ");
        String operation = bufferedReader.readLine().trim();
        if (Objects.equals(operation, "1")) {
          getKey();
          getValue();
          request = "PUT " + key + " " + value;
        } else if (Objects.equals(operation, "2")) {
          getKey();
          request = "GET " + key;
        } else if (Objects.equals(operation, "3")) {
          getKey();
          request = "DELETE " + key;
        } else if(Objects.equals(operation, "4")){
          request = "GET-ALL";
        } else if(Objects.equals(operation, "5")){
          request = "DELETE-ALL";
        }
        else {
          System.out.println("Please choose a valid operation.");
          continue;
        }

        // Send request packet to the server
        sendPacket(dataOutputStream, request);

        // Receive response packet from the server
        String response = receivePacket(dataInputStream);

        //System.out.println(response);

        if (response.startsWith("ERROR")) {
          System.out.println("Received error response from the server: " + response);
        } else {
          responseLog(response);
        }
      }
    } catch (UnknownHostException | SocketException e) {
      System.out.println("Host or Port unknown, please provide a valid hostname and port number.");
    } catch (SocketTimeoutException e) {
      System.out.println("Connection timed out. Please check the server availability and try again.");
    } catch (Exception e) {
      System.out.println("Exception occurred!" + e);
    }
  }

  /**
   * Gets the key from the user via the console input.
   *
   * @throws IOException if an error occurs during input reading
   */
  private static void getKey() throws IOException {
    System.out.print("Enter key: ");
    key = scanner.nextLine();
  }

  /**
   * Gets the value from the user via the console input.
   *
   * @throws IOException if an error occurs during input reading
   */
  private static void getValue() throws IOException {
    System.out.print("Enter Value: ");
    value = scanner.nextLine();
  }

  /**
   * Helper method to print Request messages.
   *
   * @param str message string
   */
  private static void requestLog(String str) {
    System.out.println(getTimeStamp() +
        " Request -> " + str);
  }

  /**
   * Helper method to print Response messages.
   *
   * @param str message string
   */
  private static void responseLog(String str) {
    System.out.println(getTimeStamp() +
        " Response -> " + str + "\n");
  }

  /**
   * Helper method to return the current timestamp.
   *
   * @return the current timestamp
   */
  private static String getTimeStamp() {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
    return "[Time: " + simpleDateFormat.format(new Date()) + "]";
  }

  /**
   * Helper method to send a packet to the server.
   *
   * @param outputStream the output stream to write the packet
   * @param packet       the packet to send
   * @throws IOException if an error occurs during writing
   */
  private static void sendPacket(DataOutputStream outputStream, String packet) throws IOException {
    outputStream.writeUTF(packet);
    outputStream.flush();
    requestLog(packet);
  }

  /**
   * Helper method to receive a packet from the server.
   *
   * @param inputStream the input stream to read the packet
   * @return the received packet
   * @throws IOException if an error occurs during reading
   */
  private static String receivePacket(DataInputStream inputStream) throws IOException {
    return inputStream.readUTF();
  }

  /**
   * Generates a CRC32 checksum for a given byte array.
   *
   * @param bytes The byte array for which to generate the checksum.
   * @return The CRC32 checksum value as a long.
   * @throws NullPointerException if the provided byte array is null.
   */
  private static long generateChecksum(byte[] bytes) {
    Checksum crc32 = new CRC32();
    crc32.update(bytes, 0, bytes.length);
    return crc32.getValue();
  }

  /**
   * Generates a byte array consisting of a checksum followed by the request data.
   *
   * @param request  The byte array representing the request data.
   * @param checksum The checksum value to be prepended to the request data.
   * @return A new byte array containing the checksum followed by the request
   *         data.
   * @throws NullPointerException if either the request or checksum is null.
   */
  private static byte[] generateData(byte[] request, long checksum) {
    byte[] data = new byte[request.length + 8];
    byte[] cs = ByteBuffer.allocate(8).putLong(checksum).array();

    int i = 0;

    for (byte b : cs) {
      data[i] = b;
      i++;
    }

    for (byte b : request) {
      data[i] = b;
      i++;
    }

    return data;
  }
}
