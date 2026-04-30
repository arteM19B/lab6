package Commands;

import CollectionManager.CollectionManager;
import Interfases.Command;

/**
 * Команда удаления последнего элемента коллекции.
 * Удаляет маршрут, находящийся в конце коллекции.
 *
 * @author artem_bahetkin
 * @version 1.0
 */
public class RemoveLastCommand implements Command {
    private final CollectionManager<Long> collectionManager;

    public RemoveLastCommand(CollectionManager<Long> collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(Object argument) {
        if (collectionManager.getCollection().isEmpty()) return "Коллекция пуста";
        collectionManager.remove_last();
        return "Последний элемент удалён";
    }

    @Override
    public String toString() {
        return "удалить последний элемент из коллекции";
    }
}
