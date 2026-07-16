import React, { useState } from 'react';
import { useQuery, useMutation, gql } from '@apollo/client';
import './App.css';

const GET_BOOKS = gql`
  query GetBooks {
    books {
      id
      title
      author {
        id
        name
      }
    }
  }
`;

const GET_AUTHORS = gql`
  query GetAuthors {
    authors {
      id
      name
      books {
        id
        title
      }
    }
  }
`;

const ADD_BOOK = gql`
  mutation AddBook($title: String!, $authorId: ID!) {
    addBook(title: $title, authorId: $authorId) {
      id
      title
      author {
        id
        name
      }
    }
  }
`;

const ADD_AUTHOR = gql`
  mutation AddAuthor($name: String!) {
    addAuthor(name: $name) {
      id
      name
    }
  }
`;

function BooksPanel() {
  const { loading, error, data, refetch } = useQuery(GET_BOOKS);
  const { data: authorsData } = useQuery(GET_AUTHORS);
  const [addBook] = useMutation(ADD_BOOK);

  const [showForm, setShowForm] = useState(false);
  const [title, setTitle] = useState('');
  const [authorId, setAuthorId] = useState('');

  const handleAddBook = async (e) => {
    e.preventDefault();
    if (!title.trim() || !authorId) return;
    await addBook({ variables: { title, authorId } });
    setTitle('');
    setAuthorId('');
    setShowForm(false);
    refetch();
  };

  if (loading) return <p className="status">Loading books...</p>;
  if (error) return <p className="status error">Error: {error.message}</p>;

  return (
    <div className="panel">
      <div className="panel-header">
        <h2>Books</h2>
        <button className="btn" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancel' : '+ Add Book'}
        </button>
      </div>

      {showForm && (
        <form className="form" onSubmit={handleAddBook}>
          <input
            className="input"
            type="text"
            placeholder="Book title"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            required
          />
          <select
            className="input"
            value={authorId}
            onChange={(e) => setAuthorId(e.target.value)}
            required
          >
            <option value="">Select author...</option>
            {authorsData?.authors.map((a) => (
              <option key={a.id} value={a.id}>{a.name}</option>
            ))}
          </select>
          <button className="btn btn-primary" type="submit">Add Book</button>
        </form>
      )}

      <ul className="list">
        {data.books.map((book) => (
          <li key={book.id} className="list-item">
            <span className="item-title">{book.title}</span>
            <span className="item-sub">by {book.author.name}</span>
          </li>
        ))}
      </ul>
    </div>
  );
}

function AuthorsPanel() {
  const { loading, error, data, refetch } = useQuery(GET_AUTHORS);
  const [addAuthor] = useMutation(ADD_AUTHOR);

  const [showForm, setShowForm] = useState(false);
  const [name, setName] = useState('');

  const handleAddAuthor = async (e) => {
    e.preventDefault();
    if (!name.trim()) return;
    await addAuthor({ variables: { name } });
    setName('');
    setShowForm(false);
    refetch();
  };

  if (loading) return <p className="status">Loading authors...</p>;
  if (error) return <p className="status error">Error: {error.message}</p>;

  return (
    <div className="panel">
      <div className="panel-header">
        <h2>Authors</h2>
        <button className="btn" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancel' : '+ Add Author'}
        </button>
      </div>

      {showForm && (
        <form className="form" onSubmit={handleAddAuthor}>
          <input
            className="input"
            type="text"
            placeholder="Author name"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
          />
          <button className="btn btn-primary" type="submit">Add Author</button>
        </form>
      )}

      <ul className="list">
        {data.authors.map((author) => (
          <li key={author.id} className="list-item">
            <span className="item-title">{author.name}</span>
            <span className="item-sub">{author.books.length} book{author.books.length !== 1 ? 's' : ''}</span>
          </li>
        ))}
      </ul>
    </div>
  );
}

function App() {
  return (
    <div className="app">
      <header className="app-header">
        <h1>GraphQL Book Library</h1>
        <p>Spring Boot + GraphQL + React + Apollo Client</p>
      </header>
      <div className="panels">
        <BooksPanel />
        <AuthorsPanel />
      </div>
    </div>
  );
}

export default App;
