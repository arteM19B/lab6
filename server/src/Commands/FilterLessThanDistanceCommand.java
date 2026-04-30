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
    private long distance = -1;

    public FilterLessThanDistanceCommand(CollectionManager<Long> collectionManager) {
        this.collectionManager = collectionManager;
    }

    public void setArgument(long distance) {
        this.distance = distance;
    }

    @Override
    public String execute() {
        if (distance <= 1) return "Ошибка: укажите корректное расстояние (>1).";
        return collectionManager.filterLessThanDistance(distance);
    }

    @Override
    public String toString() {
        return "вывести элементы, значение поля distance которых меньше заданного";
    }
}
