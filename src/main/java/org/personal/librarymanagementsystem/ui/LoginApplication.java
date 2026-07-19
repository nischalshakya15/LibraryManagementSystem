package org.personal.librarymanagementsystem.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.personal.librarymanagementsystem.service.impl.BookService;
import org.personal.librarymanagementsystem.service.impl.UserService;
import org.personal.librarymanagementsystem.util.NotificationUtil;

public class LoginApplication extends Application {
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/personal/librarymanagementsystem/login/login.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 640, 480);
            stage.setTitle("Library Management System");
            stage.setScene(scene);
            stage.show();
            stage.setResizable(false);
            stage.setFullScreen(false);

            Rectangle2D screenBounds =
                    Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - 640) / 2);
            stage.setY((screenBounds.getHeight() - 480) / 2);

            UserService.initialize();
            BookService.initialize();
        } catch (Exception e) {
            NotificationUtil.showErrorNotification("Error", "Error occurred in login, error: " + e.getMessage());
        }
    }
}
