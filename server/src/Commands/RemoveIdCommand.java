package Commands;

import Collection.Route;
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
    private Long id;

    public RemoveIdCommand(CollectionManager<Long> collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute() {
        boolean found = collectionManager.getCollection().removeIf(route -> route.getId().equals(id));
        return found ? "Элемент с ID " + id + " удален." : "Элемент с ID " + id + " не найден.";
    }

    public void setArgument(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Удалить элемент из коллекции по его id";
    }
}
