package Commands;

import Collection.Route;
import CollectionManager.CollectionManager;
import Interfases.Command;

/**
 * Команда добавления нового элемента в коллекцию.
 * Добавляет новый маршрут, запрашивая все необходимые поля у пользователя.
 *
 * @author artem_bahetkin
 * @version 1.0
 */
public class AddCommand implements Command {
    private final CollectionManager<Long> collectionManager;

    public AddCommand(CollectionManager<Long> collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(Object argument) {
        if (!(argument instanceof Route)) {
            return "Ошибка: для add нужен объект Route";
        }
        Route<Long> route = (Route<Long>) argument;
        route.setId(collectionManager.generateNextId());
        collectionManager.add(route);
        return "Маршрут добавлен";
    }

    @Override
    public String toString() {
        return "добавить новый элемент в коллекцию";
    }
}
