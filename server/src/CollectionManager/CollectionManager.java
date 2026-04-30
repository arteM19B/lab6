package CollectionManager;

import Collection.Coordinates;
import Collection.IdGenerator;
import Collection.Location;
import Collection.Route;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;
/**
 * Основной класс управления коллекцией маршрутов.
 * Предоставляет функциональность для добавления, удаления, сортировки,
 * фильтрации, сохранения и загрузки коллекции из XML-файла.
 *
 * @author artem_bahetkin
 * @version 1.0
 */
public class CollectionManager <T extends Number>{
    private final LinkedList<Route<T>> collection = new LinkedList<>();
    private final LocalDateTime initializationTime;
    private String fileName;
    private static CollectionManager<Long> instance;
    private final IdGenerator<T> idGenerator = new IdGenerator<>();

    public CollectionManager(String fileName) {
        this.initializationTime = LocalDateTime.now();
        this.fileName = fileName;
        instance = (CollectionManager<Long>) this;
    }

    public CollectionManager() {
        this.initializationTime = LocalDateTime.now();
        instance = (CollectionManager<Long>) this;
    }

    public static CollectionManager<Long> getInstance() {
        return instance;
    }

    public T generateNextId() {
        return idGenerator.next();
    }

    public void add(Route<T> route) {
        collection.add(route);
        System.out.println("Added route: " + route.toString());
    }

    public void show() {
        for (Route<T> routes : collection) {
            System.out.println(routes);
        }
    }

    public void removeAt(int index) {
        if (index < 0 || index >= collection.size()) {
            System.out.println("Неверный индекс");
            return;
        }
        collection.remove(index);
        System.out.println("Коллекция на позиции " + index + "удалена");
    }

    public void update(T id, Route<T> newRoute) {
        for (int i = 0; i < collection.size(); i++) {
            if (collection.get(i).getId().equals(id)) {
                collection.set(i, newRoute);
                System.out.println("Обновлённая дорога: " + newRoute.toString());
                return;
            }
        }
        System.out.println("Не найдено дороги с таким id: " + id);
    }

    public void clear() {
        collection.clear();
        System.out.println("Коллекция очищена");
    }

    public void remove_last() {
        if  (collection.isEmpty()) {
            System.out.println("Коллекция пуста, удалять нечего");
        } else {
            collection.removeLast();
            System.out.println("Последний элемент коллекции удалён");
        }
    }

    public int size() {
        return collection.size();
    }

    public String getFileName() {
        return fileName;
    }

    public String getType() {
        return collection.getClass().getSimpleName();
    }

    public LocalDateTime getInitializationTime() {
        return initializationTime;
    }

    public LinkedList<Route<T>> getCollection() {
        return collection;
    }

    public Route<T> getById(T id) {
        for (Route<T> route : collection) {
            if (route.getId().equals(id)) {
                return route;
            }
        }
        return null;
    }

    public void sort() {
        Collections.sort(collection);
        System.out.println("Коллекция отсортирована по естественному порядку (distance)");
    }

    public int removeAllByDistance(long distance) {
        int count = (int) collection.stream().filter(route -> route.getDistance() == distance).count();
        collection.removeIf(route -> route.getDistance() == distance);
        return count;
    }

    public int countGreaterThanDistance(long distance) {
        return (int) collection.stream().filter(route -> route.getDistance() > distance).count();
    }

    public String filterLessThanDistance(long distance) {
        int count = 0;
        StringBuilder sb = new StringBuilder();
        for (Route<T> route : collection) {
            if (route.getDistance() < distance) {
                count++;
                sb.append(route.toString()).append("\n");
            }
        }
        sb.append("Выведено " + String.valueOf(count) + " команд");
        return sb.toString();
    }

    public void save()  {
        if (fileName == null || fileName.isEmpty()) {
            System.out.println("Имя файла не найдено");
            return;
        }
        File file = new File(fileName);
        if (!file.isFile()) {
            System.out.println("Ошибка: " + fileName + " не является файлом");
            return;
        }
        if (!file.canWrite()) {
            System.out.println("Ошибка: нет доступа для записи в файл");
            return;
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<routes>\n");
            for (Route<T> route : collection) {
                writer.write(route.toXML() + "\n");
            }
            writer.write("</routes>\n");
            System.out.println("Коллекция успешно сохранена в " + fileName);
        } catch (IOException e) {
            System.out.println("Ошибка сохранения: " + e.getMessage());
        }
    }

    public void load() {
        if (fileName == null || fileName.isEmpty()) {
            System.out.println("Имя файла не задано.");
            return;
        }

        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("Файл " + fileName + " не найден. Коллекция пустая.");
            return;
        }
        if (!file.canRead()) {
            System.out.println("Ошибка: невозможно прочитать данные из файла");
            return;
        }
        collection.clear();
        long maxId = 0;

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                if (line.equals("<route>")) {
                    Route<T> route = parseRoute(scanner);
                    if (route != null) {
                        collection.add(route);
                        if (route.getId().longValue() > maxId) {
                            maxId = route.getId().longValue();
                        }
                    }
                }
            }

            idGenerator.setCounter(maxId);
            System.out.println("Загружено " + collection.size() + " элементов из " + fileName);

        } catch (Exception e) {
            System.out.println("Ошибка загрузки XML: " + e.getMessage());
            collection.clear();
        }
    }

    public Route<T> parseRoute(Scanner scanner) {
        Long id = 0L;
        String name = null;
        Coordinates coordinates = null;
        LocalDate creationDate;
        Location from = null;
        Location to = null;
        long distance = 0;

        try {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                if (line.equals("</route>")) {
                    break;
                }

                if (line.startsWith("<id>")) {
                    id = Long.parseLong(getTagValue(line));
                } else if (line.startsWith("<name>")) {
                    name = getTagValue(line);
                } else if (line.startsWith("<creationDate>")) {
                    creationDate = LocalDate.parse(getTagValue(line));
                } else if (line.startsWith("<distance>")) {
                    distance = Long.parseLong(getTagValue(line));
                } else if (line.equals("<coordinates>")) {
                    coordinates = parseCoordinates(scanner);
                } else if (line.startsWith("<from>")) {
                    from = parseLocation(line);
                } else if (line.startsWith("<to>")) {
                    to = parseLocation(line);
                }
            }

            if (name == null || coordinates == null || distance <= 1) {
                System.out.println("Пропущен некорректный маршрут (id=" + id + ")");
                return null;
            }

            if (id > 0) {
                return new Route<T>((T) id, name, coordinates, from, to, distance);
            } else {
                return new Route<>(name, coordinates, from, to, distance);
            }

        } catch (Exception e) {
            System.out.println("Ошибка парсинга маршрута: " + e.getMessage());
            return null;
        }
    }

    private Coordinates parseCoordinates(Scanner scanner) {
        Long x = null;
        Integer y = null;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.equals("</coordinates>")) break;

            if (line.startsWith("<x>")) x = Long.parseLong(getTagValue(line));
            if (line.startsWith("<y>")) y = Integer.parseInt(getTagValue(line));
        }

        if (x == null || y == null) return null;
        return new Coordinates(x, y);
    }


    private Location parseLocation(String line) {
        String fullText = getTagValue(line);

        if (fullText.isEmpty() || fullText.equalsIgnoreCase("null")) {
            return null;
        }

        if (fullText.contains("(") && fullText.contains(")")) {
            try {
                int open = fullText.lastIndexOf('(');
                int close = fullText.lastIndexOf(')');

                String name = fullText.substring(0, open).trim();
                String coordsStr = fullText.substring(open + 1, close).trim();

                String[] coords = coordsStr.split(",");
                if (coords.length != 2) {
                    System.out.println("Неверный формат координат Collection.Location: " + fullText);
                    return null;
                }

                float x = Float.parseFloat(coords[0].trim());
                double y = Double.parseDouble(coords[1].trim());

                if (name.isEmpty()) name = null;

                return new Location(x, y, name);

            } catch (Exception e) {
                System.out.println("Ошибка при парсинге Collection.Location: " + fullText);
                System.out.println("Причина: " + e.getMessage());
            }
        }

        System.out.println("Неизвестный формат Collection.Location: " + fullText);
        return null;
    }
    private String getTagValue(String line) {
        return line.replaceAll("<[^>]+>", "").trim();
    }

}
