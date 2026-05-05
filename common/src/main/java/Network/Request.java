package Network;

import java.io.Serializable;

public class Request implements Serializable {
    private final CommandType commandType;
    private final CommandArgument commandArgument;
    private final String requestId;

    public Request(String requestId, CommandType commandType, CommandArgument commandArgument) {
        this.commandType = commandType;
        this.commandArgument = commandArgument;
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public CommandArgument getCommandArgument() {
        return commandArgument;
    }
}
