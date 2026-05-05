package main;

import Network.CommandArgument;
import Network.CommandType;
import Network.IntegerArgument;
import Network.LongArgument;
import Network.NoArgument;
import Network.Request;
import Network.Response;
import Network.RouteArgument;
import Network.UpdateArgument;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class ClientMain {
    private static final int SERVER_TIMEOUT_MS = 5000;
    private static final Logger logger = LoggerFactory.getLogger(ClientMain.class);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RouteBuilder routeBuilder = new RouteBuilder(scanner, true);

        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(SERVER_TIMEOUT_MS);
            InetAddress address = InetAddress.getByName("localhost");
            int port = 1234;

            logger.info("Client started");

            while (true) {
                System.out.print("> ");
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) {
                    continue;
                }

                String[] parts = line.split("\\s+", 2);
                String commandName = parts[0].toLowerCase(Locale.ROOT);
                String rawArgument = parts.length > 1 ? parts[1].trim() : null;

                if (commandName.equals("exit")) {
                    break;
                }

                if (commandName.equals("save")) {
                    System.out.println("Command save is available only on server");
                    continue;
                }

                Optional<CommandType> commandType = CommandType.fromUserName(commandName);
                if (!commandType.isPresent()) {
                    logger.warn("Unknown command: {}", commandName);
                    continue;
                }

                CommandArgument argument = buildArgument(commandType.get(), rawArgument, routeBuilder);
                if (argument == null) {
                    continue;
                }

                String requestId = UUID.randomUUID().toString();
                MDC.put("requestId", requestId);

                try {
                    logger.info("Preparing command: {}", commandType.get());

                    Request request = new Request(requestId, commandType.get(), argument);

                    logger.debug("Serializing request");
                    byte[] data = serialize(request);

                    DatagramPacket packet = new DatagramPacket(data, data.length, address, port);

                    logger.info("Sending request to {}:{}", address.getHostName(), port);
                    socket.send(packet);

                    logger.info("Waiting for server response");

                    byte[] buffer = new byte[65536];
                    DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
                    socket.receive(receivePacket);

                    byte[] responseData = Arrays.copyOf(receivePacket.getData(), receivePacket.getLength());
                    Response response = deserializeResponse(responseData);

                    logger.info("Response received");
                    System.out.println(response.getMessage());
                } catch (SocketTimeoutException e) {
                    System.err.println("Server is temporarily unavailable. Try again later.");
                    logger.warn("Server response timeout", e);
                } catch (Exception e) {
                    System.err.println("Could not process server response: " + e.getMessage());
                    logger.error("Network or serialization error", e);
                } finally {
                    MDC.clear();
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        logger.info("Client stopped");
    }

    private static CommandArgument buildArgument(
            CommandType commandType,
            String rawArgument,
            RouteBuilder routeBuilder
    ) {
        logger.debug("Building argument for command: {}", commandType);
        switch (commandType) {
            case ADD:
                return new RouteArgument(routeBuilder.build());
            case UPDATE:
                Long updateId = parseLongArgument(rawArgument, "id");
                if (updateId == null || updateId <= 0) {
                    if (updateId != null) {
                        System.out.println("Error: id must be greater than 0");
                    }
                    return null;
                }
                return new UpdateArgument(updateId, routeBuilder.build());
            case REMOVE_BY_ID:
                Long id = parseLongArgument(rawArgument, "id");
                if (id != null && id <= 0) {
                    System.out.println("Error: id must be greater than 0");
                    return null;
                }
                return id == null ? null : new LongArgument(id);
            case REMOVE_AT:
                Integer index = parseIntegerArgument(rawArgument, "index");
                if (index != null && index < 0) {
                    System.out.println("Error: index must be greater than or equal to 0");
                    return null;
                }
                return index == null ? null : new IntegerArgument(index);
            case REMOVE_ALL_BY_DISTANCE:
            case COUNT_GREATER_THAN_DISTANCE:
            case FILTER_LESS_THAN_DISTANCE:
                Long distance = parseLongArgument(rawArgument, "distance");
                if (distance != null && distance <= 1) {
                    System.out.println("Error: distance must be greater than 1");
                    return null;
                }
                return distance == null ? null : new LongArgument(distance);
            default:
                return new NoArgument();
        }
    }

    private static Long parseLongArgument(String rawArgument, String name) {
        if (rawArgument == null || rawArgument.isEmpty()) {
            System.out.println("Error: " + name + " argument is required");
            return null;
        }
        try {
            return Long.parseLong(rawArgument);
        } catch (NumberFormatException e) {
            System.out.println("Error: " + name + " must be a number");
            logger.warn("Invalid number argument '{}': {}", name, rawArgument);
            return null;
        }
    }

    private static Integer parseIntegerArgument(String rawArgument, String name) {
        if (rawArgument == null || rawArgument.isEmpty()) {
            System.out.println("Error: " + name + " argument is required");
            return null;
        }
        try {
            return Integer.parseInt(rawArgument);
        } catch (NumberFormatException e) {
            System.out.println("Error: " + name + " must be an integer number");
            logger.warn("Missing argument: {}", name);
            return null;
        }
    }

    private static Response deserializeResponse(byte[] data) throws IOException, ClassNotFoundException {
        logger.debug("Response deserialization started");
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (Response) ois.readObject();
        }
    }

    private static byte[] serialize(Request request) throws IOException {
        logger.debug("Request serialization started");
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(request);
            return bos.toByteArray();
        }
    }
}
