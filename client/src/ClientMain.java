import Network.Request;
import Network.Response;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class ClientMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RouteBuilder routeBuilder = new RouteBuilder(scanner, true);

        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress address = InetAddress.getByName("localhost");
            int port = 1234;

            System.out.println("Клиент запущен");

            while (true) {
                System.out.print("> ");
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\s+", 2);
                String commandName = parts[0];

                if (commandName.equals("exit")) break;

                Object argument = null;
                if (commandName.equals("add")) {
                    argument = routeBuilder.build();
                } else if (parts.length > 1) {
                    argument = parts[1];
                }

                Request request = new Request(commandName, argument);
                byte[] data = serialize(request);

                DatagramPacket packet = new DatagramPacket(data, data.length, address, port);
                socket.send(packet);

                try {
                    byte[] buffer = new byte[65536];
                    DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

                    socket.receive(receivePacket);

                    Response response = deserializeResponse(receivePacket.getData());
                    System.out.println(response.getMessage());
                } catch (Exception e) {
                    System.err.println("Сервер не отвечает. Попробуйте позже." + e);
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private static Response deserializeResponse(byte[] data) throws IOException,  ClassNotFoundException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (Response) ois.readObject();
        }
    }

    private static byte[] serialize(Request request) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(request);
            return bos.toByteArray();
        }
    }
}
