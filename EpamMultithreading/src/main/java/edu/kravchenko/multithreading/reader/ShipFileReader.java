package edu.kravchenko.multithreading.reader;

import edu.kravchenko.multithreading.exception.SeaportException;
import edu.kravchenko.multithreading.validator.ShipFileValidator;
import edu.kravchenko.multithreading.validator.impl.ShipFileValidatorImpl;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ShipFileReader {
    private static final Logger logger = LogManager.getLogger();

    public ShipFileReader() {
    }

    public List<String> readFile(String filePath) throws SeaportException {
        ShipFileValidator shipFileValidator = new ShipFileValidatorImpl();
        if (!shipFileValidator.isValidFile(filePath)) {
            throw new SeaportException("File path represents invalid file");
        }
        Path path = Paths.get(filePath);
        List<String> validLines;
        try (Stream<String> validLinesStream = Files.lines(path)) {
            validLines = validLinesStream.collect(Collectors.toList());
        } catch (IOException e) {
            throw new SeaportException("Error while reading file" + filePath, e);
        }
        logger.log(Level.INFO, "Lines were successfully received");
        return validLines;
    }
}
