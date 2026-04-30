package Network;

import java.io.Serializable;

public class Request implements Serializable {
    private final String commandName;
    private final Object commandArg;

    public Request(String commandName, Object commandArg) {
        this.commandName = commandName;
        this.commandArg = commandArg;
    }

    public String getCommandName() {
        return commandName;
    }

    public Object getCommandArg() {
        return commandArg;
    }
}
