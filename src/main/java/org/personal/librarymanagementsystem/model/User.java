package org.personal.librarymanagementsystem.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User extends BaseModel {

    private String username;

    private String password;

    private String roles;

    public User(Long id, String username, String password, String roles) {
        super(id);
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public User(String username, String password, String roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }
}
