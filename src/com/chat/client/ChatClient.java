package com.chat.client;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

public class ChatClient {
  private static final Logger LOGGER = Logger.getLogger(ChatClient.class);
  private final int PORT = 5000;
  private final String ipAddress = "127.0.0.1";
  private final SocketAddress socketAddress = new InetSocketAddress(ipAddress, PORT);

  public static void main(String[] args) {
    BasicConfigurator.configure();
    ChatClient c = new ChatClient();
    c.run();
  }

  public void establishConnection() {
    try (Socket socket = new Socket(ipAddress, PORT);
         Scanner scanner = new Scanner(System.in)) {
      System.out.println("connected \npress l for log in");

      PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
      Thread sendThread = new Thread(new MessageSender(scanner, writer));
      sendThread.start();
      receiveMessage(socket);
    } catch (IOException e) {
      LOGGER.error("Cant establish connection!!", e);
    }
  }

  private void receiveMessage(Socket socket) {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
      while (true) {
        String received = reader.readLine();
        if (received.equalsIgnoreCase("you are disconnected")) {
          System.out.println("disconnected");
          break;
        }
        if (received.equalsIgnoreCase("this username is free and you are logged in !!!")) {
          showAvailableCommand();
        }
        System.out.println(received);
      }
    } catch (IOException e) {
      LOGGER.error("problem with the connection", e);
    }
  }

  public void run() {
    establishConnection();
  }

  public int getPORT() {
    return PORT;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  private void showAvailableCommand() {
    System.out.println("-------------------------available commands-------------------");
    System.out.println("send-username-message -> изпраща лично съобщение до даден активен потребител");
    System.out.println("sendall-message -> изпраща съобщение до всички активни потребители");
    System.out.println("listusers – извежда списък с всички активни в момента потребители");
    System.out.println("disconnect –> потребителят напуска чата");
    System.out.println("-------------------------------------------------------------");
  }
}
