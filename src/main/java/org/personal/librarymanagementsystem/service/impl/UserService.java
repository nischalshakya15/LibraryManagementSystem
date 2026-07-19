package org.personal.librarymanagementsystem.service.impl;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.personal.librarymanagementsystem.exception.UserNotFoundException;
import org.personal.librarymanagementsystem.model.User;
import org.personal.librarymanagementsystem.service.IUserService;

import java.util.Optional;
import java.util.stream.Collectors;

public class UserService implements IUserService {

    private static ObservableList<User> users = FXCollections.observableArrayList();

    private static final FileReaderWriterService<User> fileReaderWriterService = new FileReaderWriterService<>();

    private static User activeUser;

    public static void initialize() {
        fileReaderWriterService.setFromCsv(line -> {
            String[] p = line.split(",");
            return new User(
                    Long.valueOf(p[0]),
                    p[1],
                    p[2],
                    p[3]
            );
        });
        users = fileReaderWriterService.readFromFile("csv/users.csv");
    }

    @Override
    public ObservableList<User> findAll() {
        return users;
    }

    @Override
    public void save(User user) {
        Long nextId = users.getLast().getId() + 1;
        user.setId(nextId);
        users.add(user);
    }

    @Override
    public void update(User user, Long id) {
        int currentUserIndex = findIndexOf(id);
        users.set(currentUserIndex, user);
    }

    @Override
    public void remove(Long id) {
        int currentUserIndex = findIndexOf(id);
        users.remove(currentUserIndex);
    }

    @Override
    public User findOne(Long id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        throw new UserNotFoundException("User Not Found");
    }

    public int findIndexOf(Long id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return users.indexOf(user);
            }
        }
        throw new UserNotFoundException("User Not Found");
    }

    @Override
    public ObservableList<User> search(String textToBeSearched) {
        return users.stream().filter(f ->
                f.getUsername().toLowerCase().contains(textToBeSearched) ||
                        f.getRoles().toLowerCase().contains(textToBeSearched)
        ).collect(Collectors.toCollection(
                FXCollections::observableArrayList
        ));
    }

    @Override
    public User findUserByUsernameAndPassword(String username, String password) {
        return findByUsername(username, password)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));
    }

    @Override
    public void setActiveUser(User user) {
       activeUser = user;
    }

    @Override
    public User getActiveUser() {
        return activeUser;
    }

    public Optional<User> findByUsername(String username, String password) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst();
    }

}
