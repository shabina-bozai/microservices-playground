import React, { useState } from 'react';
import { useQuery, useMutation, gql } from '@apollo/client';
import './App.css';

const GET_FEED = gql`
  query GetFeed($page: Int, $size: Int) {
    feed(page: $page, size: $size) {
      posts {
        id
        content
        likesCount
        createdAt
        author {
          id
          username
          avatarUrl
        }
        comments {
          id
          content
          createdAt
          author {
            id
            username
          }
        }
      }
      page
      size
      hasMore
    }
  }
`;

const CREATE_POST = gql`
  mutation CreatePost($content: String!, $authorId: ID!) {
    createPost(content: $content, authorId: $authorId) {
      id
      content
      likesCount
      createdAt
      author {
        id
        username
        avatarUrl
      }
      comments {
        id
        content
        createdAt
        author {
          id
          username
        }
      }
    }
  }
`;

const ADD_COMMENT = gql`
  mutation AddComment($postId: ID!, $content: String!, $authorId: ID!) {
    addComment(postId: $postId, content: $content, authorId: $authorId) {
      id
      content
      createdAt
      author {
        id
        username
      }
    }
  }
`;

const LIKE_POST = gql`
  mutation LikePost($postId: ID!) {
    likePost(postId: $postId) {
      id
      likesCount
    }
  }
`;

const AVATARS = ['🧑', '👩', '🧔', '👱', '🧕', '👴'];

function getAvatar(username) {
  let hash = 0;
  for (let i = 0; i < username.length; i++) hash += username.charCodeAt(i);
  return AVATARS[hash % AVATARS.length];
}

function formatTime(isoString) {
  try {
    const date = new Date(isoString);
    const diff = Math.floor((Date.now() - date.getTime()) / 1000);
    if (diff < 60) return `${diff}s ago`;
    if (diff < 3600) return `${Math.floor(diff / 60)}m ago`;
    if (diff < 86400) return `${Math.floor(diff / 3600)}h ago`;
    return date.toLocaleDateString();
  } catch {
    return isoString;
  }
}

function PostCard({ post, onPostUpdated }) {
  const [showComments, setShowComments] = useState(false);
  const [commentText, setCommentText] = useState('');
  const [commentAuthorId, setCommentAuthorId] = useState('1');

  const [likePost] = useMutation(LIKE_POST, {
    update(cache, { data: { likePost } }) {
      cache.modify({
        id: cache.identify(post),
        fields: {
          likesCount() { return likePost.likesCount; }
        }
      });
    }
  });

  const [addComment] = useMutation(ADD_COMMENT, {
    onCompleted() {
      setCommentText('');
      if (onPostUpdated) onPostUpdated();
    }
  });

  const handleLike = () => {
    likePost({ variables: { postId: post.id } });
  };

  const handleComment = async (e) => {
    e.preventDefault();
    if (!commentText.trim()) return;
    await addComment({
      variables: { postId: post.id, content: commentText, authorId: commentAuthorId }
    });
  };

  return (
    <div className="post-card">
      <div className="post-header">
        <span className="avatar">{getAvatar(post.author.username)}</span>
        <div className="post-meta">
          <span className="username">@{post.author.username}</span>
          <span className="timestamp">{formatTime(post.createdAt)}</span>
        </div>
      </div>

      <p className="post-content">{post.content}</p>

      <div className="post-actions">
        <button className="action-btn like-btn" onClick={handleLike}>
          ❤️ {post.likesCount}
        </button>
        <button
          className="action-btn"
          onClick={() => setShowComments(!showComments)}
        >
          💬 {post.comments.length} {showComments ? '▲' : '▼'}
        </button>
      </div>

      {showComments && (
        <div className="comments-section">
          {post.comments.length === 0 && (
            <p className="no-comments">No comments yet. Be first!</p>
          )}
          {post.comments.map(comment => (
            <div key={comment.id} className="comment">
              <span className="comment-author">@{comment.author.username}</span>
              <span className="comment-content">{comment.content}</span>
              <span className="comment-time">{formatTime(comment.createdAt)}</span>
            </div>
          ))}

          <form className="comment-form" onSubmit={handleComment}>
            <select
              className="author-select"
              value={commentAuthorId}
              onChange={e => setCommentAuthorId(e.target.value)}
            >
              <option value="1">alice</option>
              <option value="2">bob</option>
              <option value="3">carol</option>
            </select>
            <input
              className="comment-input"
              type="text"
              placeholder="Add a comment..."
              value={commentText}
              onChange={e => setCommentText(e.target.value)}
            />
            <button className="comment-submit" type="submit">Post</button>
          </form>
        </div>
      )}
    </div>
  );
}

function NewPostForm({ onCreated }) {
  const [content, setContent] = useState('');
  const [authorId, setAuthorId] = useState('1');
  const [createPost, { loading }] = useMutation(CREATE_POST, {
    onCompleted() {
      setContent('');
      if (onCreated) onCreated();
    }
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!content.trim()) return;
    await createPost({ variables: { content, authorId } });
  };

  return (
    <div className="new-post-form">
      <h3>Share something</h3>
      <form onSubmit={handleSubmit}>
        <div className="new-post-row">
          <select
            className="author-select"
            value={authorId}
            onChange={e => setAuthorId(e.target.value)}
          >
            <option value="1">alice</option>
            <option value="2">bob</option>
            <option value="3">carol</option>
          </select>
        </div>
        <textarea
          className="post-textarea"
          placeholder="What's on your mind?"
          value={content}
          onChange={e => setContent(e.target.value)}
          rows={3}
        />
        <button className="btn-post" type="submit" disabled={loading || !content.trim()}>
          {loading ? 'Posting...' : 'Post'}
        </button>
      </form>
    </div>
  );
}

function Feed() {
  const [page, setPage] = useState(0);
  const [allPosts, setAllPosts] = useState(null);

  const { loading, error, data, refetch, fetchMore } = useQuery(GET_FEED, {
    variables: { page: 0, size: 5 },
    notifyOnNetworkStatusChange: true,
    onCompleted(d) {
      if (page === 0) {
        setAllPosts(d.feed.posts);
      }
    }
  });

  const handleLoadMore = async () => {
    const nextPage = page + 1;
    const result = await fetchMore({
      variables: { page: nextPage, size: 5 },
    });
    setAllPosts(prev => [...prev, ...result.data.feed.posts]);
    setPage(nextPage);
  };

  const handleRefetch = () => {
    setPage(0);
    refetch({ page: 0, size: 5 }).then(r => setAllPosts(r.data.feed.posts));
  };

  const posts = allPosts ?? data?.feed?.posts ?? [];
  const hasMore = data?.feed?.hasMore ?? false;

  if (loading && !allPosts) return <p className="status">Loading feed...</p>;
  if (error) return <p className="status error">Error: {error.message}</p>;

  return (
    <div className="feed">
      <NewPostForm onCreated={handleRefetch} />

      <div className="feed-posts">
        {posts.map(post => (
          <PostCard key={post.id} post={post} onPostUpdated={handleRefetch} />
        ))}
      </div>

      {hasMore && (
        <div className="load-more">
          <button className="btn-load-more" onClick={handleLoadMore} disabled={loading}>
            {loading ? 'Loading...' : 'Load more'}
          </button>
        </div>
      )}
    </div>
  );
}

function App() {
  return (
    <div className="app">
      <header className="app-header">
        <h1>GraphQL Social Feed</h1>
        <p>Spring Boot + GraphQL + React + Apollo Client</p>
      </header>
      <main className="app-main">
        <Feed />
      </main>
    </div>
  );
}

export default App;
