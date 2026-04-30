package Commands;

import CollectionManager.CollectionManager;
import Interfases.Command;

/**
 * Команда очистки всей коллекции.
 * Удаляет все элементы из коллекции.
 *
 * @author artem_bahetkin
 * @version 1.0
 */
public class ClearCommand implements Command {
    private final CollectionManager<Long> collectionManager;

    public ClearCommand(CollectionManager<Long> collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute() {
        collectionManager.clear();
        return "Коллекция очищена";
    }

    @Override
    public String toString() {
        return "очистить коллекцию";
    }

}
