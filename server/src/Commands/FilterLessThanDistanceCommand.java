package Commands;

import CollectionManager.CollectionManager;
import Interfases.Command;

/**
 * Команда фильтрации и вывода элементов, у которых значение поля distance
 * меньше заданного.
 *
 * @author artem_bahetkin
 * @version 1.0
 */
public class FilterLessThanDistanceCommand implements Command {
    private final CollectionManager<Long> collectionManager;

    public FilterLessThanDistanceCommand(CollectionManager<Long> collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(Object argument) {
        long distance;
        try {
            distance = Long.parseLong(argument.toString());
        } catch (Exception e) {
            return "Ошибка: укажите distance";
        }
        if (distance <= 1) return "Ошибка: укажите корректное расстояние (>1).";
        return collectionManager.filterLessThanDistance(distance);
    }

    @Override
    public String toString() {
        return "вывести элементы, значение поля distance которых меньше заданного";
    }
}
