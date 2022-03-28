package com.chat.client;

import java.io.PrintWriter;
import java.util.Scanner;

public class MessageSender implements Runnable {
    private Scanner scanner;
    private PrintWriter writer;

    public MessageSender(Scanner scanner, PrintWriter writer) {
        this.scanner = scanner;
        this.writer = writer;
    }

    public void sendMessage() {
        while (true) {
            String input = scanner.nextLine();
            writer.println(input);
            System.out.println("message is sending... ");
            if (input.equalsIgnoreCase("disconnect")) {
                System.out.println("client send disconnect");
                break;
            }
        }
    }

    @Override
    public void run() {
        sendMessage();
    }
}
