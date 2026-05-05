package main.Commands;

import main.CollectionManager.CollectionManager;
import Interfases.Command;
import Network.CommandArgument;

public class ClearCommand implements Command {
    private final CollectionManager<Long> collectionManager;

    public ClearCommand(CollectionManager<Long> collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(CommandArgument argument) {
        collectionManager.clear();
        return "Collection cleared";
    }

    @Override
    public String toString() {
        return "clear collection";
    }
}
