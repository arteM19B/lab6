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
    private Route<Long> routeToAdd;

    public AddCommand(CollectionManager<Long> collectionManager) {
        this.collectionManager = collectionManager;
    }

    public void setArg(Object arg) {
        this.routeToAdd = (Route<Long>) arg;
    }

    @Override
    public String execute(Object argument) {
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
