package org.personal.librarymanagementsystem.service;

import javafx.collections.ObservableList;
import org.personal.librarymanagementsystem.model.Book;

public interface IBookService {

    ObservableList<Book> findAll();

    void save(Book book);

    void update(Book book, Long id);

    void remove(Long id);

    Book findOne(Long id);

    ObservableList<Book> search(String textToBeSearched);

    void exportToCsv(String filePath, ObservableList<Book> rows);

}
