package edu.kravchenko.multithreading.parser;

import edu.kravchenko.multithreading.entity.Ship;
import edu.kravchenko.multithreading.exception.SeaportException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

public class ShipParser {
    private static final Logger logger = LogManager.getLogger();

    public ShipParser() {
    }

    public List<Ship> parseShips(List<String> shipLines) throws SeaportException {
        List<Ship> shipList;
        try {
            shipList = shipLines.stream()
                    .map(Ship.TaskType::valueOf)
                    .map(Ship::new)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new SeaportException("Error while parsing ship lines");
        }
        logger.log(Level.INFO, "Lines parsed successfully");
        return shipList;
    }
}
