package Commands;

import Collection.Route;
import CollectionManager.CollectionManager;
import Exceptions.ExitException;
import Interfases.Command;

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
    private Long id = -1L;
    private Route<Long> newRoute = null;

    public UpdateCommand(CollectionManager<Long> collectionManager) {
        this.collectionManager = collectionManager;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setNewRoute(Route<Long> newRoute) {
        this.newRoute = newRoute;
    };

    @Override
    public String execute() {
        if (id <= 0 || newRoute == null) {
            return "Ошибка: ID или данные не получены";
        }
        Route<Long> existing = collectionManager.getById(id);
        if (existing == null) {return "Маршрут с ID " + id + " не найден";}

        newRoute.setId(id);
        collectionManager.update(id, newRoute);
        return "Маршрут с ID " + id + " успешно добавлен";
    }

    @Override
    public String toString() {
        return "обновить значение элемента коллекции, id которого равен заданному";
    }


}
