package org.personal.librarymanagementsystem.service.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Setter;
import org.personal.librarymanagementsystem.service.IFileReadWriteService;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Setter
public class FileReaderWriterService<T> implements IFileReadWriteService<T> {

    private Function<String, T> fromCsv;

    private Function<T, String> toCsv;

    @Override
    public ObservableList<T> readFromFile(String filePath) {
        try (Stream<String> lines = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            return lines
                    .skip(1)
                    .filter(line -> !line.isBlank())
                    .map(fromCsv)
                    .collect(Collectors.toCollection(
                            FXCollections::observableArrayList
                    ));
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + filePath, e);
        }
    }

    @Override
    public void writeToFile(String headers, List<T> rows, String filePath) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {
            writer.write(headers);
            writer.newLine();
            for (T row : rows) {
                writer.write(toCsv.apply(row));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing file", e);
        }
    }
}
