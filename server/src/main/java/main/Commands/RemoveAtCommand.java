package main.Commands;

import main.CollectionManager.CollectionManager;
import Interfases.Command;
import Network.CommandArgument;
import Network.IntegerArgument;

public class RemoveAtCommand implements Command {
    private final CollectionManager<Long> collectionManager;

    public RemoveAtCommand(CollectionManager<Long> collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(CommandArgument argument) {
        if (!(argument instanceof IntegerArgument)) {
            return "Error: index argument is required";
        }

        int index = ((IntegerArgument) argument).getValue();
        if (index < 0 || index >= collectionManager.size()) {
            return "Error: element with this index does not exist";
        }

        collectionManager.removeAt(index);
        return "Element at index " + index + " removed";
    }

    @Override
    public String toString() {
        return "remove element at the given index";
    }
}
