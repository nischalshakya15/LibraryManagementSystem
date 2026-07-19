package org.personal.librarymanagementsystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.personal.librarymanagementsystem.model.User;
import org.personal.librarymanagementsystem.service.IUserService;
import org.personal.librarymanagementsystem.service.impl.UserService;

import java.util.Objects;

public class LoginController {

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private final IUserService userService = new UserService();

    @FXML
    protected void onLoginIn(ActionEvent event) {
        String username = usernameTextField.getText();
        String password = passwordField.getText();

        try {
            User activeUser = userService.findUserByUsernameAndPassword(username, password);
            userService.setActiveUser(activeUser);

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();
            Parent root = FXMLLoader.load(
                    Objects.requireNonNull(
                            getClass().getResource("/org/personal/librarymanagementsystem/dashboard/dashboard.fxml")
                    )
            );

            Stage dashboardStage = new Stage();
            dashboardStage.setScene(new Scene(root));
            dashboardStage.setTitle("Dashboard");
            dashboardStage.setMaximized(true);
            dashboardStage.show();
            stage.close();
        } catch (Exception e) {
            errorLabel.setVisible(true);
            errorLabel.setManaged(true);
            errorLabel.setText("Username or Password Invalid");
        }
    }
}
