package org.personal.librarymanagementsystem.service.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.personal.librarymanagementsystem.exception.BookNotFoundException;
import org.personal.librarymanagementsystem.model.Book;
import org.personal.librarymanagementsystem.service.IBookService;

import java.util.stream.Collectors;

public class BookService implements IBookService {

    private static ObservableList<Book> books = FXCollections.observableArrayList();

    private static final FileReaderWriterService<Book> fileReaderWriterService = new FileReaderWriterService<>();

    public static void initialize() {
        fileReaderWriterService.setFromCsv(line -> {
            String[] p = line.split(",");
            return new Book(
                    Long.valueOf(p[0]),
                    p[1],
                    p[2],
                    p[3],
                    p[4],
                    Integer.parseInt(p[5]),
                    Double.parseDouble(p[6])
            );
        });
        books = fileReaderWriterService.readFromFile("csv/books.csv");
    }

    @Override
    public ObservableList<Book> findAll() {
        return books;
    }

    @Override
    public void save(Book book) {
        Long nextId = books.getLast().getId() + 1;
        book.setId(nextId);
        books.add(book);
    }

    @Override
    public void update(Book book, Long id) {
        int currentBookIndex = findIndexOf(id);
        books.set(currentBookIndex, book);
    }

    @Override
    public void remove(Long id) {
        int currentBookIndex = findIndexOf(id);
        books.remove(currentBookIndex);
    }

    @Override
    public Book findOne(Long id) {
        for (Book book : books) {
            if (book.getId().equals(id)) {
                return book;
            }
        }
        throw new BookNotFoundException("Book with id not found");
    }

    @Override
    public ObservableList<Book> search(String textToBeSearched) {
        return books.stream().filter(f ->
                f.getName().toLowerCase().contains(textToBeSearched) ||
                        f.getAuthor().toLowerCase().contains(textToBeSearched)
                        || String.valueOf(f.getId()).toLowerCase().contains(textToBeSearched)
                        || f.getPublication().toLowerCase().contains(textToBeSearched)
                        || f.getGenre().toLowerCase().contains(textToBeSearched)
                        || String.valueOf(f.getYear()).toLowerCase().contains(textToBeSearched)
                        || String.valueOf(f.getPrice()).toLowerCase().contains(textToBeSearched)
        ).collect(Collectors.toCollection(
                FXCollections::observableArrayList
        ));
    }

    @Override
    public void exportToCsv(String filePath, ObservableList<Book> rows) {
        fileReaderWriterService.setToCsv(
                book -> String.join(",",
                        String.valueOf(book.getId()),
                        book.getName(),
                        book.getAuthor(),
                        book.getPublication(),
                        book.getGenre(),
                        String.valueOf(book.getYear()),
                        String.valueOf(book.getPrice())
                )
        );
        String headers = String.join(",", "id", "name", "author", "publication", "genre", "year", "price");
        fileReaderWriterService.writeToFile(headers, rows, filePath);
    }

    private int findIndexOf(Long id) {
        for (Book book : books) {
            if (book.getId().equals(id)) {
                return books.indexOf(book);
            }
        }
        throw new BookNotFoundException("Book with id not found");
    }

}
