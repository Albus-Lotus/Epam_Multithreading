package edu.kravchenko.multithreading.entity;

import edu.kravchenko.multithreading.util.ShipIdGenerator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Ship implements Runnable {
    private static final Logger logger = LogManager.getLogger();
    private final int shipId;
    private TaskType task;
    private StateType state;

    public enum TaskType {
        LOAD, UNLOAD
    }

    public enum StateType {
        WAITING, IN_PROCESS, COMPLETE
    }

    public Ship(TaskType task) {
        shipId = ShipIdGenerator.generateId();
        this.task = task;
        state = StateType.WAITING;
    }

    @Override
    public void run() {
        state = StateType.IN_PROCESS;
        logger.log(Level.INFO, "Ship {} is in process", shipId);
        Seaport seaport = Seaport.getInstance();
        Pier pier = seaport.obtainPier();
        pier.processShip(this);
        seaport.releasePier(pier);
        state = StateType.COMPLETE;
        logger.log(Level.INFO, "Ship {} completed", shipId);
    }

    public int getShipId() {
        return shipId;
    }

    public TaskType getTask() {
        return task;
    }

    public StateType getState() {
        return state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ship ship = (Ship) o;
        return task == ship.task;
    }

    @Override
    public int hashCode() {
        return task.hashCode();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Ship{");
        sb.append("shipId=").append(shipId);
        sb.append(", taskType=").append(task);
        sb.append(", stateType=").append(state);
        sb.append('}');
        return sb.toString();
    }
}
