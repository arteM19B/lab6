import CollectionManager.CollectionManager;
import CollectionManager.Invoker;
import Commands.*;
import Network.Request;
import Network.Response;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class ServerMain {
    private static final int PORT = 1234;
    public static void main(String[] args) {
        CollectionManager<Long> collectionManager= new CollectionManager<Long>("collection.xml");
        collectionManager.load();

        Invoker invoker = new Invoker();

        invoker.registerCommand("show", new ShowCommand(collectionManager));
        invoker.registerCommand("add", new AddCommand(collectionManager));
        invoker.registerCommand("clear", new ClearCommand(collectionManager));
        invoker.registerCommand("remove_by_id", new RemoveIdCommand(collectionManager));



        try (DatagramChannel channel = DatagramChannel.open()) {
            channel.configureBlocking(false);
            channel.bind(new InetSocketAddress(PORT));

            ByteBuffer buffer = ByteBuffer.allocate(65536);

            while (true) {
                buffer.clear();

                SocketAddress clientAddress = channel.receive(buffer);

                if (clientAddress != null) {
                    buffer.flip();

                    byte[] data = new byte[buffer.remaining()];
                    buffer.get(data);
                    Request request = deserializeRequest(data);

                    // Логика выполнения, регистрация, передача аргументов, получение результата

                    String resultText = "Команда " + request.getCommandName() + " выполнена успешно  // заглушка";

                    Response response = new Response(resultText);
                    byte[] responseBytes = serializeResponse(response);

                    ByteBuffer responseBuffer = ByteBuffer.wrap(responseBytes);
                    channel.send(responseBuffer, clientAddress);
                }

            }
        } catch (Exception e) {

        }
    }

    private static Request deserializeRequest(byte[] data) throws IOException,  ClassNotFoundException {
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
