package org.personal.librarymanagementsystem.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.personal.librarymanagementsystem.model.User;
import org.personal.librarymanagementsystem.service.IUserService;
import org.personal.librarymanagementsystem.service.impl.UserService;
import org.personal.librarymanagementsystem.util.NotificationUtil;

import java.util.Arrays;
import java.util.List;

public class UserController {

    @FXML
    private TextField searchTextField;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private ComboBox<String> roleComboBox;

    @FXML
    private Button addButton;

    @FXML
    private Button editOrUpdateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, Long> idColumn;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> passwordColumn;

    @FXML
    private TableColumn<User, String> roleColumn;

    private final IUserService userService = new UserService();

    @FXML
    protected void initialize() {
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("roles"));

        defaultInitialization();
    }

    @FXML
    protected void save() {
        try {
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();
            String role = roleComboBox.getSelectionModel().getSelectedItem();
            userService.save(new User(username, password, role));
            NotificationUtil.showSuccessNotification("Success", "User added successfully");
            defaultInitialization();
        } catch (Exception e) {
            NotificationUtil.showErrorNotification("Error", "Error occurred when saving user, error: " + e.getMessage());
        }
    }

    @FXML
    protected void editOrUpdate() {
        try {
            User user = userTable.getSelectionModel().getSelectedItem();
            if (user != null && editOrUpdateButton.getText().equals("Edit")) {
                usernameTextField.setText(user.getUsername());
                passwordTextField.setText(user.getPassword());
                roleComboBox.setValue(user.getRoles());
                addButton.setDisable(true);
                deleteButton.setDisable(true);
                editOrUpdateButton.setText("Update");
            } else {
                User updateUser = userService.findOne(user.getId());
                updateUser.setUsername(usernameTextField.getText());
                updateUser.setPassword(passwordTextField.getText());
                updateUser.setRoles(roleComboBox.getSelectionModel().getSelectedItem());
                userService.update(updateUser, updateUser.getId());
                NotificationUtil.showSuccessNotification("Success", "User updated successfully");
                defaultInitialization();
            }
        } catch (Exception e) {
            NotificationUtil.showErrorNotification("Error", "Error occurred when edit or update user, error: " + e.getMessage());
        }
    }

    @FXML
    protected void remove() {
        try {
            Long id = userTable.getSelectionModel().getSelectedItem().getId();
            userService.remove(id);
            NotificationUtil.showSuccessNotification("Success", "User removed successfully");
            defaultInitialization();
        } catch (Exception e) {
            NotificationUtil.showErrorNotification("Error", "Error occurred when removing user, error: " + e.getMessage());
        }
    }

    @FXML
    protected void search() {
        try {
            String textToBeSearched = searchTextField.getText().trim().toLowerCase();
            if (!textToBeSearched.isEmpty()) {
                userTable.setItems(userService.search(textToBeSearched));
            } else {
                userTable.setItems(userService.findAll());
            }
        } catch (Exception e) {
            NotificationUtil.showSuccessNotification("Success", "Error occurred when searching user, error: " + e.getMessage());
        }
    }

    @FXML
    protected void defaultInitialization() {
        Arrays.asList(
                usernameTextField,
                passwordTextField,
                searchTextField
        ).forEach(TextField::clear);
        roleComboBox.setItems(FXCollections.observableArrayList
                ("ROLE_ADMIN", "ROLE_USER")
        );

        addButton.setDisable(false);
        disableEditOrDeleteButton(true);
        editOrUpdateButton.setText("Edit");

        userTable.getSelectionModel().clearSelection();
        userTable.setItems(userService.findAll());
        userTable.setOnMouseClicked(event -> {
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
