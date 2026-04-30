package Commands;

import CollectionManager.CollectionManager;
import Interfases.Command;

/**
 * Команда удаления всех элементов, у которых значение поля distance
 * равно заданному.
 *
 * @author artem_bahetkin
 * @version 1.0
 */
public class RemoveAllByDistanceCommand implements Command {

    private final CollectionManager<Long> collectionManager;
    private long distance = -1;

    public RemoveAllByDistanceCommand(CollectionManager<Long> collectionManager) {
        this.collectionManager = collectionManager;
    }

    public void setArgument(long distance) {
        this.distance = distance;
    }

    @Override
    public String execute() {
        if (distance <= 1) return "Ошибка: укажите корректное расстояние (>1).";

        int count = collectionManager.removeAllByDistance(distance);
        if (count == 0) return "Дорог с таким расстоянием не найдено.";

        return "Удалено " + count + " дорог с расстоянием " + distance + ".";
    }

    @Override
    public String toString() {
        return "удалить из коллекции все элементы, значение поля distance которого эквивалентно заданному";
    }
}
