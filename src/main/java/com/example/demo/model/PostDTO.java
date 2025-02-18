package com.example.demo.model;

import java.util.Objects;

public class PostDTO {
    private String content;
    private String author;

    // constructors
    public PostDTO(String content, String author) {
        this.content = content;
        this.author = author;
    }

    // Getter and Setter
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    // toString
    @Override
    public String toString() {
        return "PostDTO{" +
                "content='" + content + '\'' +
                ", author='" + author + '\'' +
                '}';
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostDTO postDTO = (PostDTO) o;
        return Objects.equals(content, postDTO.content) &&
               Objects.equals(author, postDTO.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, author);
    }
}