package Commands;

import CollectionManager.CollectionManager;
import Interfases.Command;

/**
 * Команда подсчёта количества элементов, у которых значение поля distance
 * больше заданного.
 *
 * @author artem_bahetkin
 * @version 1.0
 */
public class CounGreaterThanDistanceCommand implements Command {

    private final CollectionManager<Long> collectionManager;

    public CounGreaterThanDistanceCommand(CollectionManager<Long> collectionManager) {
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

        int count = collectionManager.countGreaterThanDistance(distance);
        if (count == 0) return "Дорог с расстоянием больше " + distance + " не найдено.";

        return "Найдено " + count + " дорог с расстоянием, большим, чем " + distance + ".";
    }
    @Override
    public String toString() {
        return "вывести количество элементов, значение поля distance которых больше заданного";
    }
}
