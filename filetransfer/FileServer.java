package filetransfer;

import java.io.*;
import java.net.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer {
    public static final String FILES_PATH = "sendfiles";
    public static final int PORT = 3000;
    private ServerSocket serverSocket;

    public FileServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            acceptConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void acceptConnection() throws IOException {
        while (true) {
            Socket clientSocket = serverSocket.accept();
            if (clientSocket.isConnected())
                new Thread(() -> {
                    ClientConnection client = new ClientConnection(clientSocket);
                    client.sendFile();
                }).start();
        }
    }

    public static void main(String[] args) {
        new FileServer();
    }
}
