package main.CollectionManager;

import Collection.Route;

import java.util.Scanner;

public class RouteXMLParser {
    private final CollectionManager<Long> collectionManager;
    private final Scanner scanner;

    public RouteXMLParser(CollectionManager<Long> collectionManager, Scanner scanner) {
        this.collectionManager = collectionManager;
        this.scanner = scanner;
    }

    public static Route<Long> parse(Scanner scanner) throws Exception {
        StringBuilder xml = new StringBuilder();
        boolean insideRoute = false;

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (line.startsWith("<route>")) {
                insideRoute = true;
            }

            if (insideRoute) {
                xml.append(line).append("\n");
            }

            if (line.endsWith("</route>")) {
                break;
            }
        }
        if (xml.isEmpty()) {
            throw new Exception("Не найден xml блок");
        }

        return parseXmlString(xml.toString());
    }

    private static Route<Long> parseXmlString(String xmlString) {
        Scanner tempScanner = new Scanner(xmlString);
        return CollectionManager.getInstance().parseRoute(tempScanner);
    }
}
