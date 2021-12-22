package edu.kravchenko.multithreading.entity;

import edu.kravchenko.multithreading.util.PierIdGenerator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class Pier {
    private static final Logger logger = LogManager.getLogger();
    private final int pierId;

    public Pier() {
        pierId = PierIdGenerator.generateId();
    }

    public void processShip(Ship ship) {
        logger.log(Level.INFO, "Pier {} starts processing ship {}", pierId, ship.getShipId());
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            logger.log(Level.INFO, "Error while processing: {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
        Seaport seaport = Seaport.getInstance();
        switch (ship.getTask()) {
            case LOAD -> seaport.loadContainer();
            case UNLOAD -> seaport.unloadContainer();
        }
        logger.log(Level.INFO, "Pier {} completes processing ship {}", pierId, ship.getShipId());
    }

    public int getPierId() {
        return pierId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Pier{");
        sb.append("pierId=").append(pierId);
        sb.append('}');
        return sb.toString();
    }
}
