package Interfases;

/**
 * Интерфейс, который реализуют все команды программы.
 * Реализует паттерн "Interfases.Command" для унифицированного выполнения действий.
 *
 * @author artem_bahetkin
 * @version 1.0
 */
public interface Command {
    String execute();
}
