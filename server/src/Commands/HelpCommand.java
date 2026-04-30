package Commands;

import Interfases.Command;

import java.util.Map;
/**
 * Команда вывода справки по доступным командам.
 * Выводит список всех зарегистрированных команд и их описания.
 *
 * @author artem_bahetkin
 * @version 1.0
 */
public class HelpCommand implements Command {
    private final Map<String, Command> commands;

    public HelpCommand(Map<String, Command> commands) {
        this.commands = commands;
    }

    @Override
    public String execute(Object argument) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            sb.append(entry.getKey()).append(" -- ").append(entry.getValue().toString()).append("\n");
        }
        return sb.toString();
    }
}
