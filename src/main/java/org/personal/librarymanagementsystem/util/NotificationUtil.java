package org.personal.librarymanagementsystem.util;

import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

public class NotificationUtil {

    public static void showSuccessNotification(String title, String message) {
        Notifications.create()
                .title(title)
                .text(message)
                .position(Pos.BOTTOM_RIGHT)
                .hideAfter(Duration.seconds(3))
                .showInformation();
    }

    public static void showErrorNotification(String title, String message) {
        Notifications.create()
                .title(title)
                .text(message)
                .position(Pos.BOTTOM_RIGHT)
                .hideAfter(Duration.seconds(3))
                .showError();
    }
}
