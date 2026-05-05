package main.Commands;

import main.CollectionManager.CollectionManager;
import Interfases.Command;
import Network.CommandArgument;

public class SortCommand implements Command {
    private final CollectionManager<Long> collectionManager;

    public SortCommand(CollectionManager<Long> collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(CommandArgument argument) {
        collectionManager.sort();
        return "Collection sorted";
    }

    @Override
    public String toString() {
        return "sort collection by natural order";
    }
}
