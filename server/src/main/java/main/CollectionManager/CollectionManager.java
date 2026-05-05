package main.CollectionManager;

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
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CollectionManager<T extends Number> {
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
        System.out.println("Added route: " + route);
    }

    public void show() {
        getCollectionSortedByLocation().forEach(System.out::println);
    }

    public void removeAt(int index) {
        collection.remove(index);
        System.out.println("Element at index " + index + " removed");
    }

    public void update(T id, Route<T> newRoute) {
        int index = IntStream.range(0, collection.size())
                .filter(i -> collection.get(i).getId().equals(id))
                .findFirst()
                .orElse(-1);

        if (index >= 0) {
            collection.set(index, newRoute);
            System.out.println("Updated route: " + newRoute);
        } else {
            System.out.println("Route with id " + id + " not found");
        }
    }

    public void clear() {
        collection.clear();
        System.out.println("Collection cleared");
    }

    public void remove_last() {
        if (collection.isEmpty()) {
            System.out.println("Collection is empty");
        } else {
            collection.removeLast();
            System.out.println("Last element removed");
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

    public LinkedList<Route<T>> getCollectionSortedByLocation() {
        return collection.stream()
                .sorted(locationComparator())
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public Route<T> getById(T id) {
        return collection.stream()
                .filter(route -> route.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void sort() {
        LinkedList<Route<T>> sorted = collection.stream()
                .sorted()
                .collect(Collectors.toCollection(LinkedList::new));
        collection.clear();
        collection.addAll(sorted);
        System.out.println("Collection sorted by distance");
    }

    public int removeAllByDistance(long distance) {
        int count = (int) collection.stream()
                .filter(route -> route.getDistance() == distance)
                .count();
        collection.removeIf(route -> route.getDistance() == distance);
        return count;
    }

    public int countGreaterThanDistance(long distance) {
        return (int) collection.stream()
                .filter(route -> route.getDistance() > distance)
                .count();
    }

    public String filterLessThanDistance(long distance) {
        LinkedList<Route<T>> filtered = collection.stream()
                .filter(route -> route.getDistance() < distance)
                .sorted(locationComparator())
                .collect(Collectors.toCollection(LinkedList::new));

        String routes = filtered.stream()
                .map(Route::toString)
                .collect(Collectors.joining("\n"));

        return routes + (routes.isEmpty() ? "" : "\n") + "Shown " + filtered.size() + " elements";
    }

    public void save() {
        if (fileName == null || fileName.isEmpty()) {
            System.out.println("File name is not set");
            return;
        }

        File file = new File(fileName);
        if (file.exists() && !file.isFile()) {
            System.out.println("Error: " + fileName + " is not a file");
            return;
        }
        if (file.exists() && !file.canWrite()) {
            System.out.println("Error: file is not writable");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            writer.write("<routes>\n");
            String routesXml = collection.stream()
                    .map(Route::toXML)
                    .collect(Collectors.joining("\n"));
            if (!routesXml.isEmpty()) {
                writer.write(routesXml + "\n");
            }
            writer.write("</routes>\n");
            System.out.println("Collection saved to " + fileName);
        } catch (IOException e) {
            System.out.println("Save error: " + e.getMessage());
        }
    }

    public void load() {
        if (fileName == null || fileName.isEmpty()) {
            System.out.println("File name is not set");
            return;
        }

        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("File " + fileName + " not found. Collection is empty.");
            return;
        }
        if (!file.canRead()) {
            System.out.println("Error: file is not readable");
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
            System.out.println("Loaded " + collection.size() + " elements from " + fileName);
        } catch (Exception e) {
            System.out.println("XML load error: " + e.getMessage());
            collection.clear();
        }
    }

    public Route<T> parseRoute(Scanner scanner) {
        Long id = 0L;
        String name = null;
        Coordinates coordinates = null;
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
                    LocalDate.parse(getTagValue(line));
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
                System.out.println("Skipped invalid route id=" + id);
                return null;
            }

            if (id > 0) {
                return new Route<>((T) id, name, coordinates, from, to, distance);
            }
            return new Route<>(name, coordinates, from, to, distance);
        } catch (Exception e) {
            System.out.println("Route parse error: " + e.getMessage());
            return null;
        }
    }

    private Coordinates parseCoordinates(Scanner scanner) {
        Long x = null;
        Integer y = null;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.equals("</coordinates>")) {
                break;
            }
            if (line.startsWith("<x>")) {
                x = Long.parseLong(getTagValue(line));
            }
            if (line.startsWith("<y>")) {
                y = Integer.parseInt(getTagValue(line));
            }
        }

        if (x == null || y == null) {
            return null;
        }
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
                    System.out.println("Invalid location format: " + fullText);
                    return null;
                }

                float x = Float.parseFloat(coords[0].trim());
                double y = Double.parseDouble(coords[1].trim());

                if (name.isEmpty()) {
                    name = null;
                }
                return new Location(x, y, name);
            } catch (Exception e) {
                System.out.println("Location parse error: " + e.getMessage());
            }
        }

        System.out.println("Unknown location format: " + fullText);
        return null;
    }

    private Comparator<Route<T>> locationComparator() {
        return Comparator.comparing((Route<T> route) -> locationKey(route.getFrom()))
                .thenComparing(route -> locationKey(route.getTo()))
                .thenComparing(Route::getName);
    }

    private String locationKey(Location location) {
        return location == null ? "" : location.toString();
    }

    private String getTagValue(String line) {
        return line.replaceAll("<[^>]+>", "").trim();
    }
}
