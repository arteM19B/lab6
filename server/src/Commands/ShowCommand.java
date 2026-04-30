package Commands;

import CollectionManager.CollectionManager;
import Interfases.Command;

/**
 * Команда вывода всех элементов коллекции.
 * Выводит в стандартный поток вывода все маршруты в строковом представлении.
 *
 * @author artem_bahetkin
 * @version 1.0
 */
public class ShowCommand implements Command {
    private final CollectionManager<Long> collectionManager;

    public ShowCommand(CollectionManager<Long> collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute() {
        if (collectionManager.getCollection().isEmpty()) {
            return "Коллекция пуста";
        }
        StringBuilder sb = new StringBuilder();
        collectionManager.getCollection().forEach(route -> {sb.append(route.toString()).append("\n");});
        return sb.toString();
    }

    @Override
    public String toString() {
        return "вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }
}
