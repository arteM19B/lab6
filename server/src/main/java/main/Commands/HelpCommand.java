package main.Commands;

import Interfases.Command;
import Network.CommandArgument;
import Network.CommandType;

import java.util.Map;
import java.util.stream.Collectors;

public class HelpCommand implements Command {
    private final Map<CommandType, Command> commands;

    public HelpCommand(Map<CommandType, Command> commands) {
        this.commands = commands;
    }

    @Override
    public String execute(CommandArgument argument) {
        return commands.entrySet().stream()
                .map(entry -> entry.getKey().getUserName() + " -- " + entry.getValue())
                .collect(Collectors.joining("\n"));
    }

    @Override
    public String toString() {
        return "show available client commands";
    }
}
