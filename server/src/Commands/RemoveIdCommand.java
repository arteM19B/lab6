package Commands;

import CollectionManager.CollectionManager;
import Interfases.Command;

import java.util.Iterator;
/**
 * Команда удаления элемента коллекции по его ID.
 * Удаляет маршрут с указанным идентификатором.
 *
 * @author artem_bahetkin
 * @version 1.0
 */
public class RemoveIdCommand implements Command {
    private final CollectionManager<Long> collectionManager;

    public RemoveIdCommand(CollectionManager<Long> collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(Object argument) {
        Long id;
        try {
            id = Long.parseLong(argument.toString());
        } catch (Exception e) {
            return "Ошибка: укажите ID";
        }

        boolean found = collectionManager.getCollection().removeIf(route -> route.getId().equals(id));
        return found ? "Элемент с ID " + id + " удалён" : "Элемент с ID " + id + " не найден";
    }

    @Override
    public String toString() {
        return "удалить элемент по id";
    }
}
