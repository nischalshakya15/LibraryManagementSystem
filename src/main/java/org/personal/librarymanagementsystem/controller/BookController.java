package org.personal.librarymanagementsystem.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.personal.librarymanagementsystem.model.Book;
import org.personal.librarymanagementsystem.service.IBookService;
import org.personal.librarymanagementsystem.service.impl.BookService;
import org.personal.librarymanagementsystem.util.NotificationUtil;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class BookController {

    @FXML
    private Button addButton;

    @FXML
    private Button editOrUpdateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button exportToCsvButton;

    @FXML
    private TableView<Book> bookTable;

    @FXML
    private TableColumn<Book, Long> idColumn;

    @FXML
    private TableColumn<Book, String> nameColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;

    @FXML
    private TableColumn<Book, String> publicationColumn;

    @FXML
    private TableColumn<Book, String> genreColumn;

    @FXML
    private TableColumn<Book, String> yearColumn;

    @FXML
    private TableColumn<Book, Double> priceColumn;

    @FXML
    private TextField nameTextFiled;

    @FXML
    private TextField authorTextField;

    @FXML
    private TextField publicationTextField;

    @FXML
    private TextField genreTextField;

    @FXML
    private TextField yearTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private TextField searchTextField;

    private final IBookService bookService = new BookService();

    @FXML
    protected void initialize() {
        bookTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        publicationColumn.setCellValueFactory(new PropertyValueFactory<>("publication"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        defaultInitialization();
    }

    @FXML
    protected void save() {
        try {
            Book book = new Book(
                    nameTextFiled.getText(),
                    authorTextField.getText(),
                    publicationTextField.getText(),
                    genreTextField.getText(),
                    Integer.parseInt(yearTextField.getText()),
                    Double.parseDouble(priceTextField.getText())
            );
            bookService.save(book);
            NotificationUtil.showSuccessNotification("Success", "Book added successfully");

            defaultInitialization();
        } catch (Exception e) {
            NotificationUtil.showErrorNotification("Error", "Error occurred when saving book, error: " + e.getMessage());
        }
    }

    @FXML
    protected void editOrUpdate() {
        try {
            Book book = bookTable.getSelectionModel().getSelectedItem();
            if (book != null & editOrUpdateButton.getText().equalsIgnoreCase("Edit")) {
                addButton.setDisable(true);
                deleteButton.setDisable(true);
                exportToCsvButton.setDisable(true);

                editOrUpdateButton.setText("Update");
                nameTextFiled.setText(book.getName());
                authorTextField.setText(book.getAuthor());
                publicationTextField.setText(book.getPublication());
                genreTextField.setText(book.getGenre());
                yearTextField.setText(String.valueOf(book.getYear()));
                priceTextField.setText(String.valueOf(book.getPrice()));
            } else {
                Book updateBook = bookService.findOne(book.getId());
                updateBook.setName(nameTextFiled.getText());
                updateBook.setAuthor(authorTextField.getText());
                updateBook.setPublication(publicationTextField.getText());
                updateBook.setGenre(genreTextField.getText());
                updateBook.setYear(Integer.parseInt(yearTextField.getText()));
                updateBook.setPrice(Double.parseDouble(priceTextField.getText()));

                bookService.update(updateBook, book.getId());

                bookTable.setEditable(true);

                NotificationUtil.showSuccessNotification("Update", "Book update successfully");
                defaultInitialization();
            }


        } catch (Exception e) {
            NotificationUtil.showErrorNotification("Error", "Error occurred when saving or update book, error: " + e.getMessage());
        }
    }

    @FXML
    protected void remove() {
        try {
            Book book = bookTable.getSelectionModel().getSelectedItem();
            bookService.remove(book.getId());
            NotificationUtil.showSuccessNotification("Remove", "Book removed successfully");
            defaultInitialization();
        } catch (Exception e) {
            NotificationUtil.showErrorNotification("Error", "Error occurred when removing book, error: " + e.getMessage());
        }
    }

    @FXML
    protected void search() {
        try {
            disableEditOrDeleteButton(true);
            String textToBeSearched = searchTextField.getText().toLowerCase();
            if (!textToBeSearched.isEmpty()) {
                ObservableList<Book> filteredBooks = bookService.search(textToBeSearched);
                bookTable.setItems(filteredBooks);
            } else {
                ObservableList<Book> books = bookService.findAll();
                bookTable.setItems(books);
            }
        } catch (Exception e) {
            NotificationUtil.showErrorNotification("Error", "Error occurred when searching book, error: " + e.getMessage());
        }
    }


    @FXML
    protected void exportToCsv() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Export CSV File");
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("CSV Files", "*.csv")
            );
            fileChooser.setInitialFileName("books.csv");

            Stage stage = (Stage) bookTable.getScene().getWindow();
            File file = fileChooser.showSaveDialog(stage);

            if (file == null) {
                return;
            }

            String filePath = file.getAbsolutePath();
            bookService.exportToCsv(filePath, bookTable.getItems());
            NotificationUtil.showSuccessNotification("Success", "Export completed successfully.");
            defaultInitialization();
        } catch (Exception e) {
            NotificationUtil.showErrorNotification("Error", "Error occurred when exporting to CSV file, error: " + e.getMessage());
        }
    }

    @FXML
    protected void defaultInitialization() {
        Arrays.asList(
                nameTextFiled,
                authorTextField,
                publicationTextField,
                genreTextField,
                yearTextField,
                priceTextField,
                searchTextField
        ).forEach(TextField::clear);

        addButton.setDisable(false);
        editOrUpdateButton.setText("Edit");
        exportToCsvButton.setDisable(false);
        disableEditOrDeleteButton(true);

        bookTable.setItems(bookService.findAll());
        bookTable.getSelectionModel().clearSelection();
        bookTable.setOnMouseClicked(event -> {
            disableEditOrDeleteButton(false);
        });
    }

    @FXML
    protected void disableEditOrDeleteButton(boolean isDisable) {
        List<Button> buttons = Arrays.asList(editOrUpdateButton, deleteButton);
        for (Button button : buttons) {
            button.setDisable(isDisable);
        }
    }

}
