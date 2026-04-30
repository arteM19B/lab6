package Commands;

import CollectionManager.CollectionManager;
import Interfases.Command;

/**
 * Команда сортировки коллекции.
 * Сортирует коллекцию маршрутов в естественном порядке (по расстоянию).
 *
 * @author artem_bahetkin
 * @version 1.0
 */
public class SortCommand implements Command {
    private final CollectionManager<Long> collectionManager;
    public SortCommand(CollectionManager<Long> collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(Object argument) {
        collectionManager.sort();
        return "Коллекция отсортирована";
    }

    @Override
    public String toString() {
        return "отсортировать коллекцию в естественном порядке";
    }
}
