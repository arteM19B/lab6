import CollectionManager.CollectionManager;
import CollectionManager.Invoker;
import Commands.*;
import Network.Request;
import Network.Response;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ServerMain {
    private static final int PORT = 1234;

    public static void main(String[] args) {
        CollectionManager<Long> collectionManager = new CollectionManager<Long>("collection.xml");
        collectionManager.load();

        Invoker invoker = new Invoker();
        registerCommands(invoker, collectionManager);

        try (DatagramChannel channel = DatagramChannel.open()) {
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(PORT));

            ByteBuffer buffer = ByteBuffer.allocate(65536);

            while (true) {
                buffer.clear();
                SocketAddress clientAddress = channel.receive(buffer);

                if (clientAddress == null) {
                    continue;
                }

                buffer.flip();

                byte[] data = new byte[buffer.remaining()];
                buffer.get(data);

                Request request = deserializeRequest(data);
                String resultText = invoker.execute(request);

                Response response = new Response(resultText);
                byte[] responseBytes = serializeResponse(response);

                ByteBuffer responseBuffer = ByteBuffer.wrap(responseBytes);
                channel.send(responseBuffer, clientAddress);
            }
        } catch (Exception e) {
            System.err.println("Ошибка сервера: " + e.getMessage());
        }
    }

    private static void registerCommands(Invoker invoker, CollectionManager<Long> collectionManager) {
        invoker.registerCommand("show", new ShowCommand(collectionManager));
        invoker.registerCommand("add", new AddCommand(collectionManager));
        invoker.registerCommand("update", new UpdateCommand(collectionManager));
        invoker.registerCommand("remove_at", new RemoveAtCommand(collectionManager));
        invoker.registerCommand("clear", new ClearCommand(collectionManager));
        invoker.registerCommand("remove_by_id", new RemoveIdCommand(collectionManager));
        invoker.registerCommand("remove_last", new RemoveLastCommand(collectionManager));
        invoker.registerCommand("sort", new SortCommand(collectionManager));
        invoker.registerCommand("save", new SaveCommand(collectionManager));
        invoker.registerCommand("info", new InfoCommand(collectionManager));
        invoker.registerCommand("remove_all_by_distance", new RemoveAllByDistanceCommand(collectionManager));
        invoker.registerCommand("count_greater_than_distance", new CounGreaterThanDistanceCommand(collectionManager));
        invoker.registerCommand("filter_less_than_distance", new FilterLessThanDistanceCommand(collectionManager));
        invoker.registerCommand("help", new HelpCommand(invoker.getCommandsMap()));
    }

    private static Request deserializeRequest(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            return (Request) ois.readObject();
        }
    }

    private static byte[] serializeResponse(Response response) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(response);
            return bos.toByteArray();
        }
    }
}
