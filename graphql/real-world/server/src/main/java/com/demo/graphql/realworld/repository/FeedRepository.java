package com.demo.graphql.realworld.repository;

import com.demo.graphql.realworld.model.Comment;
import com.demo.graphql.realworld.model.FeedPage;
import com.demo.graphql.realworld.model.Post;
import com.demo.graphql.realworld.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class FeedRepository {

    private final List<User> users = new ArrayList<>();
    private final List<Post> posts = new ArrayList<>();
    private final List<Comment> comments = new ArrayList<>();

    private final AtomicLong userIdSeq = new AtomicLong(4);
    private final AtomicLong postIdSeq = new AtomicLong(7);
    private final AtomicLong commentIdSeq = new AtomicLong(11);

    public FeedRepository() {
        // Seed users
        users.add(new User(1L, "alice", "https://api.dicebear.com/7.x/avataaars/svg?seed=alice"));
        users.add(new User(2L, "bob", "https://api.dicebear.com/7.x/avataaars/svg?seed=bob"));
        users.add(new User(3L, "carol", "https://api.dicebear.com/7.x/avataaars/svg?seed=carol"));

        LocalDateTime now = LocalDateTime.now();

        // Seed posts
        posts.add(new Post(1L, "Just started learning GraphQL — mind blown! The schema-first approach is so clean.", 1L, now.minusHours(5), 12));
        posts.add(new Post(2L, "Spring Boot 3 with virtual threads is ridiculously fast. Loving Java 21.", 2L, now.minusHours(4), 7));
        posts.add(new Post(3L, "Hot take: REST APIs are overrated. GraphQL lets clients ask for exactly what they need.", 3L, now.minusHours(3), 21));
        posts.add(new Post(4L, "Built a full-stack app with React + Apollo today. The cache invalidation is chef's kiss.", 1L, now.minusHours(2), 5));
        posts.add(new Post(5L, "Anyone else using @SchemaMapping in Spring GraphQL? It makes resolver composition so elegant.", 2L, now.minusHours(1), 9));
        posts.add(new Post(6L, "GraphiQL is genuinely one of the best developer tools ever made. Interactive docs ftw.", 3L, now.minusMinutes(30), 3));

        // Seed comments
        comments.add(new Comment(1L, "Agreed! The type system catches so many bugs at the API boundary.", 2L, 1L, now.minusHours(4)));
        comments.add(new Comment(2L, "Try subscriptions next — real-time with GraphQL is amazing.", 3L, 1L, now.minusHours(3)));
        comments.add(new Comment(3L, "Virtual threads + WebFlux is the combo I'm waiting for.", 1L, 2L, now.minusHours(3)));
        comments.add(new Comment(4L, "Both have their place IMO. REST is simpler for public APIs.", 1L, 3L, now.minusHours(2)));
        comments.add(new Comment(5L, "GraphQL over-fetching problems are real though if not careful.", 2L, 3L, now.minusHours(1)));
        comments.add(new Comment(6L, "DataLoader pattern solves the N+1 problem nicely.", 3L, 3L, now.minusMinutes(45)));
        comments.add(new Comment(7L, "Apollo DevTools + GraphiQL = debugging superpowers.", 2L, 4L, now.minusHours(1)));
        comments.add(new Comment(8L, "Yes! Beats @RestController boilerplate by a mile.", 3L, 5L, now.minusMinutes(50)));
        comments.add(new Comment(9L, "The playground auto-complete from introspection is so good.", 1L, 6L, now.minusMinutes(20)));
        comments.add(new Comment(10L, "It even generates docs from your schema descriptions. Incredible.", 2L, 6L, now.minusMinutes(10)));
    }

    public FeedPage findAllPosts(int page, int size) {
        List<Post> sorted = posts.stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .toList();
        int fromIndex = page * size;
        if (fromIndex >= sorted.size()) {
            return new FeedPage(List.of(), page, size, false);
        }
        int toIndex = Math.min(fromIndex + size, sorted.size());
        List<Post> pageItems = sorted.subList(fromIndex, toIndex);
        boolean hasMore = toIndex < sorted.size();
        return new FeedPage(pageItems, page, size, hasMore);
    }

    public Post findPostById(Long id) {
        return posts.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Comment> findCommentsByPostId(Long postId) {
        return comments.stream()
                .filter(c -> c.getPostId().equals(postId))
                .sorted(Comparator.comparing(Comment::getCreatedAt))
                .toList();
    }

    public User findUserById(Long id) {
        return users.stream().filter(u -> u.getId().equals(id)).findFirst().orElse(null);
    }

    public Post addPost(String content, Long authorId) {
        Post post = new Post(postIdSeq.getAndIncrement(), content, authorId, LocalDateTime.now(), 0);
        posts.add(post);
        return post;
    }

    public Comment addComment(String content, Long authorId, Long postId) {
        Comment comment = new Comment(commentIdSeq.getAndIncrement(), content, authorId, postId, LocalDateTime.now());
        comments.add(comment);
        return comment;
    }

    public Post likePost(Long postId) {
        Post post = findPostById(postId);
        if (post != null) {
            post.setLikesCount(post.getLikesCount() + 1);
        }
        return post;
    }
}
