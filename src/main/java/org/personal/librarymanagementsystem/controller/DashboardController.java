package org.personal.librarymanagementsystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.personal.librarymanagementsystem.model.User;
import org.personal.librarymanagementsystem.service.IUserService;
import org.personal.librarymanagementsystem.service.impl.UserService;
import org.personal.librarymanagementsystem.util.NotificationUtil;

import java.util.Objects;

public class DashboardController {

    @FXML
    private Label userLabel;

    @FXML
    private StackPane contentArea;

    private final IUserService userService = new UserService();

    @FXML
    protected void initialize() {
        showBooks();
        setUserLabel();
    }

    @FXML
    protected void showBooks() {
        try {
            Parent view = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("/org/personal/librarymanagementsystem/books/books.fxml")));
            contentArea.getChildren().setAll(view);
        } catch (Exception e) {
            NotificationUtil.showErrorNotification("Error", "Show book error: " + e.getMessage());
        }
    }

    @FXML
    private void showUser() {
        try {
            Parent view = FXMLLoader.load(
                    Objects.requireNonNull(getClass().getResource("/org/personal/librarymanagementsystem/user/user.fxml")));
            contentArea.getChildren().setAll(view);
        } catch (Exception e) {
            NotificationUtil.showErrorNotification("Error", "Show user error: " + e.getMessage());
        }
    }

    @FXML
    private void showLogin(MouseEvent event) {
        try {
            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/personal/librarymanagementsystem/login/login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 640, 480);
            stage.setTitle("Book Management System");
            stage.setScene(scene);

            stage.show();

            stage.setResizable(false);
            stage.setFullScreen(false);

            Rectangle2D screenBounds =
                    Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - 640) / 2);
            stage.setY((screenBounds.getHeight() - 480) / 2);
        } catch (Exception e) {
            NotificationUtil.showErrorNotification("Error", "Show login error: " + e.getMessage());
        }
    }

    private void setUserLabel() {
        User activerUser = userService.getActiveUser();
        if (activerUser.getRoles().equals("ROLE_ADMIN")) {
            userLabel.setVisible(true);
            userLabel.setManaged(true);
        } else {
            userLabel.setVisible(false);
            userLabel.setManaged(false);
        }
    }
}
