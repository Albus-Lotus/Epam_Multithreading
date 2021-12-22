package edu.kravchenko.multithreading.entity;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Seaport {
    private static final Logger logger = LogManager.getLogger();
    private static Seaport instance;
    private static final String PROPERTIES_FILE_PATH = "files/seaport.properties";
    private static final AtomicBoolean isCreated = new AtomicBoolean(false);
    private static final Lock locking = new ReentrantLock(true);
    private Condition freePierCondition = locking.newCondition();
    private Deque<Pier> freePiers = new ArrayDeque<>();
    private Deque<Pier> busyPiers = new ArrayDeque<>();
    private final int CAPACITY;
    private final int PIER_NUMBER;
    private AtomicInteger currentContainerNumber;

    public Seaport() {
        InputStream propertyFileStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_PATH);
        Properties properties = new Properties();
        try {
            properties.load(propertyFileStream);
        } catch (IOException e) {
            logger.log(Level.ERROR, "Input stream is invalid");
        }
        String capacity = properties.getProperty("capacity");
        String pierNumber = properties.getProperty("pier_number");
        String containerNumber = properties.getProperty("container_number");
        CAPACITY = Integer.parseInt(capacity);
        PIER_NUMBER = Integer.parseInt(pierNumber);
        currentContainerNumber = new AtomicInteger(Integer.parseInt(containerNumber));
        for (int i = 0; i < PIER_NUMBER; i++) {
            freePiers.addLast(new Pier());
        }
    }

    public static Seaport getInstance() {
        if (!isCreated.get()) {
            try {
                locking.lock();
                if (instance == null) {
                    instance = new Seaport();
                    isCreated.set(true);
                }
            } finally {
                locking.unlock();
            }
        }
        return instance;
    }

    public Pier obtainPier() {
        logger.log(Level.INFO, "Start obtaining pier");
        try {
            locking.lock();
            try {
                if (freePiers.isEmpty()) {
                    logger.log(Level.INFO, "Waiting for pier");
                    freePierCondition.await();
                }
            } catch (InterruptedException e) {
                logger.log(Level.ERROR, "Error while obtaining pier: {}", e.getMessage());
                Thread.currentThread().interrupt();
            }
            Pier pier = freePiers.removeLast();
            busyPiers.addLast(pier);
            logger.log(Level.INFO, "Pier {} were obtained", pier.getPierId());
            return pier;
        } finally {
            locking.unlock();
        }
    }

    public void releasePier(Pier pier) {
        try {
            locking.lock();
            busyPiers.remove(pier);
            freePiers.addLast(pier);
            freePierCondition.signal();
            logger.log(Level.INFO, "Pier {} were released", pier.getPierId());
        } finally {
            locking.unlock();
        }
    }

    public void loadContainer() {
        logger.log(Level.INFO, "Start load");
        while (currentContainerNumber.get() == 0) {
            logger.log(Level.DEBUG, "Waiting for load");
        }
        currentContainerNumber.decrementAndGet();
        logger.log(Level.INFO, "Complete load, container amount: {}", currentContainerNumber);
    }

    public void unloadContainer() {
        logger.log(Level.INFO, "Start unload");
        while (currentContainerNumber.get() == CAPACITY) {
            logger.log(Level.DEBUG, "Waiting for unload");
        }
        currentContainerNumber.incrementAndGet();
        logger.log(Level.INFO, "Complete unload, container amount: {}", currentContainerNumber);
    }

    public int getCapacity() {
        return CAPACITY;
    }

    public int getPierNumber() {
        return PIER_NUMBER;
    }

    public int getCurrentContainerNumber() {
        return currentContainerNumber.get();
    }

}
