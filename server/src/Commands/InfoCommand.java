package Commands;

import CollectionManager.CollectionManager;
import Interfases.Command;

/**
 * Команда вывода информации о коллекции.
 * Выводит тип коллекции, дату инициализации, количество элементов и имя файла.
 *
 * @author artem_bahetkin
 * @version 1.0
 */
public class InfoCommand implements Command {
    private final CollectionManager<Long> collectionManager;

    public InfoCommand(CollectionManager<Long> collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute() {
        StringBuilder sb = new StringBuilder();
        sb.append("Тип коллекции: ").append(collectionManager.getType()).append("\n");
        sb.append("Дата инициализации: ").append(collectionManager.getInitializationTime()).append("\n");
        sb.append("Количество элементов: ").append(collectionManager.size()).append("\n");

        if (collectionManager.getFileName() != null) {
            sb.append("Файл хранения: ").append(collectionManager.getFileName()).append("\n");
        }
        if (collectionManager.size() > 0) {
            sb.append("Тип элементов: ").append(collectionManager.getCollection().getFirst().getClass().getSimpleName());
        }

        return sb.toString();
    }

    @Override
    public String toString() {
        return "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)";
    }


}
