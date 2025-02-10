package com.example.demo.service;

import com.example.demo.model.UserDTO;
import com.example.demo.model.Post;
import com.example.demo.model.PostDTO;
import com.example.demo.model.User;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.model.UserDAOInterface;

import org.springframework.cache.annotation.Cacheable;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsersService {
    @Autowired
    private UserDAOInterface userDAO;

    @Cacheable(value = "users", key = "#id")
    public UserDTO getUserByID(int id) {
        User users = userDAO.findById(id);
        if (users == null) {
            return null;
        }
        UserDTO userDto = new UserDTO(users.getId(), users.getUsername(), users.getPassword(), users.isEnabled());
        return userDto;
    }

    public boolean findUserByUsername(String username) {
        return userDAO.findUserByUsername(username) != null;
    }

    public boolean updatePassword(String username, String password) {
        return userDAO.updatePassword(username, password);
    }

    public UserDTO findUserWithPosts(int id) {
        User users = userDAO.findUserWithPosts(id);
        if (users == null) {
            return null;
        }
        UserDTO userDto = new UserDTO(users.getId(), users.getUsername(), users.getPassword(), users.isEnabled());
        List<Post> posts = users.getPosts();
        List<PostDTO> postDTOs = posts.stream()
            .map(post -> new PostDTO(post.getContent(), post.getAuthor()))
            .collect(Collectors.toList());

        userDto.setPosts(postDTOs);
        return userDto;
    }
}
