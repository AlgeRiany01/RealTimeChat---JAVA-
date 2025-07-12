package client;

import java.io.*;
import java.net.Socket;

public class ClientSocket {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public ClientSocket(String serverIP, int serverPort) throws IOException {
        socket = new Socket(serverIP, serverPort);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream(), true);
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    public String receiveMessage() throws IOException {
        return reader.readLine();
    }

    public void close() throws IOException {
        socket.close();
    }
}
