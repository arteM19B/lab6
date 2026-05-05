package main.Commands;

import Collection.Route;
import main.CollectionManager.CollectionManager;
import Interfases.Command;
import Network.CommandArgument;

import java.util.stream.Collectors;

public class ShowCommand implements Command {
    private final CollectionManager<Long> collectionManager;

    public ShowCommand(CollectionManager<Long> collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(CommandArgument argument) {
        if (collectionManager.getCollection().isEmpty()) {
            return "Collection is empty";
        }

        return collectionManager.getCollectionSortedByLocation().stream()
                .map(Route::toString)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public String toString() {
        return "show all collection elements sorted by location";
    }
}
