package edu.kravchenko.multithreading._main;

import edu.kravchenko.multithreading.entity.Ship;
import edu.kravchenko.multithreading.exception.SeaportException;
import edu.kravchenko.multithreading.parser.ShipParser;
import edu.kravchenko.multithreading.reader.ShipFileReader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final Logger logger = LogManager.getLogger();
    private static final String FILE_PATH = "files/shipData.txt";

    public static void main(String[] args) {
        File file = new File(Main.class.getClassLoader().getResource(FILE_PATH).getFile());
        String path = file.getAbsolutePath();
        ShipFileReader shipFileReader = new ShipFileReader();
        ShipParser shipParser = new ShipParser();
        List<Ship> ships = null;
        try {
            List<String> fileLines = shipFileReader.readFile(path);
            ships = shipParser.parseShips(fileLines);
            ExecutorService service = Executors.newFixedThreadPool(ships.size());
            ships.forEach(service::execute);
            service.shutdown();
        } catch (SeaportException e) {
            logger.log(Level.ERROR, e);
        }
    }
}
