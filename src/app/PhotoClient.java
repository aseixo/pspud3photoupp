package app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;

public class PhotoClient {

    private static final int SERVER_PORT = 7000;
    private static final String SERVER_IP = "192.168.1.133";
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) throws IOException {

        InetAddress address = InetAddress.getByName(SERVER_IP);
        System.out.println("Server connected on " + address.toString());
        System.out.println("Server is reacheable " + address.isReachable(2000));
        byte[] connData = {1};
        DatagramSocket client = new DatagramSocket();
        DatagramPacket connPacket = new DatagramPacket(connData, connData.length, address, SERVER_PORT);
        client.send(connPacket);
        File file = new File("newphoto25.jpg");
        FileOutputStream fos = new FileOutputStream(file);

        String endString = "";
        while (!endString.contains("END")) {
            endString = "";
            byte[] photoBuffer = new byte[BUFFER_SIZE];
            DatagramPacket photoPacket = new DatagramPacket(photoBuffer, photoBuffer.length);
            client.receive(photoPacket);
            photoBuffer = photoPacket.getData();
            endString = new String(photoPacket.getData());
            if (endString.contains("END")) break;
            fos.write(photoPacket.getData(), 0, photoPacket.getLength());

        }
        fos.flush(); fos.close(); client.close();

    }
}

