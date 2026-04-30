package Commands;

import CollectionManager.CollectionManager;
import Interfases.Command;

/**
 * Команда сохранения коллекции в файл.
 * Сохраняет текущую коллекцию в XML-файл, указанный при запуске программы.
 *
 * @author artem_bahetkin
 * @version 1.0
 */
public class SaveCommand implements Command {
    private final CollectionManager<Long> collectionManager;

    public SaveCommand(CollectionManager<Long> collectionManager) {
        this.collectionManager = collectionManager;
    }

    @Override
    public String execute(Object argument) {
        collectionManager.save();
        return "Коллекция сохранена в файл на сервере.";
    }

    @Override
    public String toString() {
        return "сохранить коллекцию в файл";
    }
}