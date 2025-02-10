package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Post;
import com.example.demo.model.PostDTO;
import com.example.demo.model.PostRepository;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post createPost(PostDTO postDTO) {
        Post post = new Post();
        post.setContent(postDTO.getContent());
        post.setAuthor(postDTO.getAuthor());
        return postRepository.save(post);
    }

    public Post getPostByContent(String content) {
        return this.postRepository.findByContent(content);
    }
}
