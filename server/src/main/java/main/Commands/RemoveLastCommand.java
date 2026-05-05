package main.Commands;

import main.CollectionManager.CollectionManager;
import Interfases.Command;
import Network.CommandArgument;

public class RemoveLastCommand implements Command {
    private final CollectionManager<Long> collectionManager;

    public RemoveLastCommand(CollectionManager<Long> collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(CommandArgument argument) {
        if (collectionManager.getCollection().isEmpty()) {
            return "Collection is empty";
        }
        collectionManager.remove_last();
        return "Last element removed";
    }

    @Override
    public String toString() {
        return "remove last collection element";
    }
}
