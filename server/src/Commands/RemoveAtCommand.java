package Commands;

import CollectionManager.CollectionManager;
import Interfases.Command;

/**
 * Команда удаления элемента коллекции по индексу.
 * Удаляет маршрут, находящийся на указанной позиции в коллекции.
 *
 * @author artem_bahetkin
 * @version 1.0
 */
public class RemoveAtCommand implements Command {
    private final CollectionManager<Long> collectionManager;
    private int index = -1;

    public RemoveAtCommand(CollectionManager<Long> collectionManager) {
        this.collectionManager = collectionManager;
    }

    public void setArgument(int index){
        this.index = index;
    }

    @Override
    public String execute() {
        if (index < 0) {return "Ошибка: неверный индекс";}
        try {
            collectionManager.removeAt(index);
            return "Элемент по индексу " + index + " успешно удалён.";
        } catch (IndexOutOfBoundsException e) {
            return "Ошибка: элемента с таким индексом не существует.";
        }
    }

    @Override
    public String toString() {
        return "удалить элемент, находящийся в заданной позиции коллекции (index)";
    }
}
