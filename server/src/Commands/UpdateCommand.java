package Commands;

import Collection.Route;
import CollectionManager.CollectionManager;
import Interfases.Command;
import Network.UpdateArgument;

import java.util.Scanner;
/**
 * Команда обновления существующего элемента коллекции по его ID.
 * Заменяет маршрут с заданным идентификатором на новый.
 *
 * @author artem_bahetkin
 * @version 1.0
 */
public class UpdateCommand implements Command {
    private final CollectionManager<Long> collectionManager;

    public UpdateCommand(CollectionManager<Long> collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(Object argument) {
        if (!(argument instanceof UpdateArgument)) {
            return "Ошибка: для update нужны ID и объект Route";
        }

        UpdateArgument updateArgument = (UpdateArgument) argument;
        Long id = updateArgument.getId();
        Route<Long> newRoute = updateArgument.getRoute();

        if (id == null || id <= 0 || newRoute == null) {
            return "Ошибка: ID или данные маршрута не получены";
        }

        Route<Long> existing = collectionManager.getById(id);
        if (existing == null) {
            return "Маршрут с ID " + id + " не найден";
        }

        newRoute.setId(id);
        collectionManager.update(id, newRoute);
        return "Маршрут с ID " + id + " обновлён";
    }

    @Override
    public String toString() {
        return "обновить элемент коллекции по id";
    }
}
