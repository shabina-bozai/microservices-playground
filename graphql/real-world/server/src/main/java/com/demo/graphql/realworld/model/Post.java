package com.demo.graphql.realworld.model;

import java.time.LocalDateTime;

public class Post {
    private Long id;
    private String content;
    private Long authorId;
    private LocalDateTime createdAt;
    private int likesCount;

    public Post() {}

    public Post(Long id, String content, Long authorId, LocalDateTime createdAt, int likesCount) {
        this.id = id;
        this.content = content;
        this.authorId = authorId;
        this.createdAt = createdAt;
        this.likesCount = likesCount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public Long getAuthorId() { return authorId; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public int getLikesCount() { return likesCount; }
    public void setLikesCount(int likesCount) { this.likesCount = likesCount; }
}
