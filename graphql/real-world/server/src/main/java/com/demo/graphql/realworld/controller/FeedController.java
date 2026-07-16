package com.demo.graphql.realworld.controller;

import com.demo.graphql.realworld.model.Comment;
import com.demo.graphql.realworld.model.FeedPage;
import com.demo.graphql.realworld.model.Post;
import com.demo.graphql.realworld.model.User;
import com.demo.graphql.realworld.repository.FeedRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class FeedController {

    private final FeedRepository repository;

    public FeedController(FeedRepository repository) {
        this.repository = repository;
    }

    @QueryMapping
    public FeedPage feed(@Argument Integer page, @Argument Integer size) {
        int p = page != null ? page : 0;
        int s = size != null ? size : 5;
        return repository.findAllPosts(p, s);
    }

    @QueryMapping
    public Post post(@Argument Long id) {
        return repository.findPostById(id);
    }

    @MutationMapping
    public Post createPost(@Argument String content, @Argument Long authorId) {
        return repository.addPost(content, authorId);
    }

    @MutationMapping
    public Comment addComment(@Argument Long postId, @Argument String content, @Argument Long authorId) {
        return repository.addComment(content, authorId, postId);
    }

    @MutationMapping
    public Post likePost(@Argument Long postId) {
        return repository.likePost(postId);
    }

    @SchemaMapping(typeName = "Post", field = "author")
    public User postAuthor(Post post) {
        return repository.findUserById(post.getAuthorId());
    }

    @SchemaMapping(typeName = "Post", field = "comments")
    public List<Comment> postComments(Post post) {
        return repository.findCommentsByPostId(post.getId());
    }

    @SchemaMapping(typeName = "Comment", field = "author")
    public User commentAuthor(Comment comment) {
        return repository.findUserById(comment.getAuthorId());
    }

    @SchemaMapping(typeName = "Post", field = "createdAt")
    public String postCreatedAt(Post post) {
        return post.getCreatedAt().toString();
    }

    @SchemaMapping(typeName = "Comment", field = "createdAt")
    public String commentCreatedAt(Comment comment) {
        return comment.getCreatedAt().toString();
    }
}
