package com.example.demo.model;

import lombok.Data;

@Data // 自動產生 getter, setter, equals, hashCode, toString
public class PostDTO {
    private String content;
    private String author;

    public PostDTO(String content, String author) {
        this.content = content;
        this.author = author;
    }
}
