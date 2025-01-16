package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class PhotoServer {

    private static final int SERVER_PORT = 7000;
    private static final String SERVER_IP = "192.168.1.133";
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) throws IOException {

        DatagramSocket photoServer = new DatagramSocket(SERVER_PORT);

        byte[] buffer = new byte[1];
        DatagramPacket connPacket = new DatagramPacket(buffer, 1);
        photoServer.receive(connPacket);
        InetAddress clientAddress = connPacket.getAddress();
        int clientPort = connPacket.getPort();
        System.out.println("Cliente IP " + clientAddress.getHostName() + " porto " + clientPort);

        File file = new File("photo.jpg");
        FileInputStream reader = new FileInputStream(file);

        int bytes = 0;
        int numPackets = 0;

        boolean condition = true;
        while (condition) {
            byte[] picBuffer = new byte[BUFFER_SIZE];
            bytes = reader.read(picBuffer);
            if (bytes > 0) {
            DatagramPacket jpgPacket = new DatagramPacket(picBuffer, picBuffer.length, clientAddress, clientPort);
            photoServer.send(jpgPacket);
            numPackets++;
            } else break;
        }

        byte[] end;
        end = new byte[]{69, 78, 68};
        DatagramPacket endPacket = new DatagramPacket(end, end.length, clientAddress, clientPort);
        photoServer.send(endPacket);
        photoServer.close();
        System.out.println("Número de datagramas enviados ".concat(String.valueOf(numPackets)));
    }
}
