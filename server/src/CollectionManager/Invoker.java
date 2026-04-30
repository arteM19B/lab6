package CollectionManager;

import Interfases.Command;
import Network.Request;

import java.util.HashMap;
import java.util.Map;
/**
 * Класс-инвокер, реализующий паттерн Interfases.Command.
 * Хранит зарегистрированные команды и обеспечивает их поиск и выполнение по имени.
 *
 * @author arten_bahetkin
 * @version 1.0
 */
public class Invoker {
    private final Map<String, Command> commandsMap = new HashMap<>();
    public void registerCommand(String name, Command command) {
            commandsMap.put(name, command);
    }

    public Command getCommand(String name) {
        return commandsMap.get(name);
    }

    public Map<String, Command> getCommandsMap() {
        return commandsMap;
    }

    public String execute(Request request) {
        Command command = commandsMap.get(request.getCommandName());

        if (command == null) {
            return "Неизвестная команда: " + request.getCommandName();
        }

        try {
            return command.execute(request.getCommandArg());
        } catch (Exception e) {
            return "Ошибка выполнения команды " + e.getMessage();
        }
    }

}
