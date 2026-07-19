package org.personal.librarymanagementsystem.service;

import javafx.collections.ObservableList;
import org.personal.librarymanagementsystem.model.User;

public interface IUserService {

    ObservableList<User> findAll();

    void save(User user);

    void update(User user, Long id);

    void remove(Long id);

    User findOne(Long id);

    ObservableList<User> search(String textToBeSearched);

    User findUserByUsernameAndPassword(String username, String password);

     void setActiveUser(User user);

     User getActiveUser();
}
