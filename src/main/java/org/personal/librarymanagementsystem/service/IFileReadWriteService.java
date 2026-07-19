package org.personal.librarymanagementsystem.service;

import java.util.List;

public interface IFileReadWriteService<T> {

    List<T> readFromFile(String filePath);

    void writeToFile(String headers, List<T> rows, String filePath);
}
