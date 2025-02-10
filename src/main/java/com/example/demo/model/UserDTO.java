package com.example.demo.model;
import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data // 底下參數自動產生 getter, setter, equals, hashCode, toString
public class UserDTO implements Serializable{
    private static final long serialVersionUID = 1L;
    private int id;
    private String username;
    private String password;
    private boolean enabled;
    private List<PostDTO> posts;

    public UserDTO (int id, String username, String password, boolean enabled) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
    }
}