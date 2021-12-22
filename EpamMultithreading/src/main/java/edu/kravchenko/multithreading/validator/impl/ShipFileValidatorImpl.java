package edu.kravchenko.multithreading.validator.impl;

import edu.kravchenko.multithreading.validator.ShipFileValidator;

import java.io.File;

public class ShipFileValidatorImpl implements ShipFileValidator {
    public ShipFileValidatorImpl() {
    }

    @Override
    public boolean isValidFile(String filePath) {
        if (filePath == null) {
            return false;
        }
        File file = new File(filePath);
        return file.isFile() && file.length() != 0;
    }
}
