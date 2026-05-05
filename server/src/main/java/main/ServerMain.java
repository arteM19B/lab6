package main;

import main.CollectionManager.CollectionManager;
import main.CollectionManager.Invoker;
import main.Commands.*;
import Network.CommandType;
import Network.NoArgument;
import Network.Request;
import Network.Response;
import main.ServerNetwork.ReceivedPacket;
import main.ServerNetwork.RequestReader;
import main.ServerNetwork.ResponseSender;
import main.ServerNetwork.UdpRequestReceiver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class ServerMain {
    private static final int PORT = 1234;
    private static final Logger logger = LoggerFactory.getLogger(ServerMain.class);

    public static void main(String[] args) {
        logger.info("Server started");
        CollectionManager<Long> collectionManager = new CollectionManager<Long>("collection.xml");
        collectionManager.load();
        logger.info("Collection loaded");

        Invoker invoker = new Invoker();
        registerCommands(invoker, collectionManager);

        UdpRequestReceiver requestReceiver = new UdpRequestReceiver();
        RequestReader requestReader = new RequestReader();
        ResponseSender responseSender = new ResponseSender();

        try (DatagramChannel channel = DatagramChannel.open();
             BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in))) {
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(PORT));
            logger.info("Server started on port: {}", PORT);

            ByteBuffer buffer = ByteBuffer.allocate(65536);
            boolean running = true;

            while (running) {
                ReceivedPacket receivedPacket = requestReceiver.receive(channel, buffer);
                if (receivedPacket != null) {
                    try {
                        logger.info("Packet received from: {}", receivedPacket.getClientAddress());
                        Request request = requestReader.read(receivedPacket.getData());
                        MDC.put("requestId", request.getRequestId());
                        logger.info("Request received: {}", request.getRequestId());
                        Response response = new Response(invoker.execute(request));
                        responseSender.send(channel, receivedPacket, response);
                    } catch (Exception e) {
                        logger.error("Error while processing request", e);
                    } finally {
                        MDC.clear();
                    }
                }

                if (consoleReader.ready()) {
                    running = handleServerCommand(consoleReader.readLine(), collectionManager);
                }

                Thread.sleep(10);
            }
        } catch (Exception e) {
            System.err.println("Server error: " + e.getMessage());
            logger.error("Server error", e);
        } finally {
            collectionManager.save();
            logger.info("Collection saved");
            logger.info("Server stopped");
        }
    }

    private static void registerCommands(Invoker invoker, CollectionManager<Long> collectionManager) {
        invoker.registerCommand(CommandType.HELP, new HelpCommand(invoker.getCommandsMap()));
        invoker.registerCommand(CommandType.INFO, new InfoCommand(collectionManager));
        invoker.registerCommand(CommandType.SHOW, new ShowCommand(collectionManager));
        invoker.registerCommand(CommandType.ADD, new AddCommand(collectionManager));
        invoker.registerCommand(CommandType.UPDATE, new UpdateCommand(collectionManager));
        invoker.registerCommand(CommandType.REMOVE_BY_ID, new RemoveIdCommand(collectionManager));
        invoker.registerCommand(CommandType.REMOVE_AT, new RemoveAtCommand(collectionManager));
        invoker.registerCommand(CommandType.CLEAR, new ClearCommand(collectionManager));
        invoker.registerCommand(CommandType.REMOVE_LAST, new RemoveLastCommand(collectionManager));
        invoker.registerCommand(CommandType.SORT, new SortCommand(collectionManager));
        invoker.registerCommand(CommandType.REMOVE_ALL_BY_DISTANCE, new RemoveAllByDistanceCommand(collectionManager));
        invoker.registerCommand(CommandType.COUNT_GREATER_THAN_DISTANCE, new CounGreaterThanDistanceCommand(collectionManager));
        invoker.registerCommand(CommandType.FILTER_LESS_THAN_DISTANCE, new FilterLessThanDistanceCommand(collectionManager));
        logger.info("Commands registered");
    }

    private static boolean handleServerCommand(String line, CollectionManager<Long> collectionManager) {
        String command = line == null ? "" : line.trim();
        if (command.isEmpty()) {
            return true;
        }

        if (command.equals("save")) {
            System.out.println(new SaveCommand(collectionManager).execute(new NoArgument()));
            logger.info("Server console command received: save");
            return true;
        }

        if (command.equals("exit")) {
            logger.info("Server console command received: exit");
            return false;
        }

        System.out.println("Server command is not available: " + command);
        System.out.println("Available server commands: save, exit");
        logger.warn("Unknown server console command: {}", command);

        return true;
    }
}
