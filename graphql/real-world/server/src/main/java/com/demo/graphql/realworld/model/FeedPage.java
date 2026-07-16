package com.demo.graphql.realworld.model;

import java.util.List;

public class FeedPage {
    private List<Post> posts;
    private int page;
    private int size;
    private boolean hasMore;

    public FeedPage(List<Post> posts, int page, int size, boolean hasMore) {
        this.posts = posts;
        this.page = page;
        this.size = size;
        this.hasMore = hasMore;
    }

    public List<Post> getPosts() { return posts; }
    public int getPage() { return page; }
    public int getSize() { return size; }
    public boolean isHasMore() { return hasMore; }
}
