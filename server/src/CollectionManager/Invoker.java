package CollectionManager;

import Interfases.Command;

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

}
