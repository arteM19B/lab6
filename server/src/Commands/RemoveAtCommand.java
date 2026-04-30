package Commands;

import CollectionManager.CollectionManager;
import Interfases.Command;

/**
 * Команда удаления элемента коллекции по индексу.
 * Удаляет маршрут, находящийся на указанной позиции в коллекции.
 *
 * @author artem_bahetkin
 * @version 1.0
 */
public class RemoveAtCommand implements Command {
    private final CollectionManager<Long> collectionManager;

    public RemoveAtCommand(CollectionManager<Long> collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(Object argument) {
        int index;
        try {
            index = Integer.parseInt(argument.toString());
        } catch (Exception e) {
            return "Ошибка: укажите индекс";
        }

        if (index < 0 || index >= collectionManager.size()) {
            return "Ошибка: элемента с таким индексом не существует";
        }

        collectionManager.removeAt(index);
        return "Элемент по индексу " + index + " удалён";
    }

    @Override
    public String toString() {
        return "удалить элемент по индексу";
    }
}
