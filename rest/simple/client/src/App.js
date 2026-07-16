import React, { useState, useEffect } from 'react';

const styles = {
  container: { maxWidth: 600, margin: '40px auto', fontFamily: 'sans-serif', padding: '0 16px' },
  header: { fontSize: 28, fontWeight: 'bold', marginBottom: 24, color: '#1a1a2e' },
  addRow: { display: 'flex', gap: 8, marginBottom: 24 },
  input: { flex: 1, padding: '8px 12px', border: '1px solid #ccc', borderRadius: 6, fontSize: 15 },
  btn: { padding: '8px 16px', borderRadius: 6, border: 'none', cursor: 'pointer', fontSize: 14 },
  addBtn: { background: '#4a90d9', color: '#fff' },
  deleteBtn: { background: '#e74c3c', color: '#fff', marginLeft: 8 },
  saveBtn: { background: '#27ae60', color: '#fff', marginLeft: 8 },
  cancelBtn: { background: '#95a5a6', color: '#fff', marginLeft: 4 },
  todoItem: { display: 'flex', alignItems: 'center', padding: '10px 14px', marginBottom: 8, background: '#f9f9f9', borderRadius: 8, border: '1px solid #e0e0e0' },
  todoTitle: { flex: 1, marginLeft: 10, fontSize: 15 },
  completed: { textDecoration: 'line-through', color: '#888' },
  editInput: { flex: 1, marginLeft: 10, padding: '4px 8px', border: '1px solid #4a90d9', borderRadius: 4, fontSize: 15 },
  emptyMsg: { color: '#aaa', textAlign: 'center', marginTop: 32 },
};

export default function App() {
  const [todos, setTodos] = useState([]);
  const [input, setInput] = useState('');
  const [editingId, setEditingId] = useState(null);
  const [editTitle, setEditTitle] = useState('');

  useEffect(() => {
    fetchTodos();
  }, []);

  async function fetchTodos() {
    const res = await fetch('/api/todos');
    const data = await res.json();
    setTodos(data);
  }

  async function addTodo() {
    const title = input.trim();
    if (!title) return;
    const res = await fetch('/api/todos', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ title, completed: false }),
    });
    const created = await res.json();
    setTodos(prev => [...prev, created]);
    setInput('');
  }

  async function toggleCompleted(todo) {
    const res = await fetch(`/api/todos/${todo.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ title: todo.title, completed: !todo.completed }),
    });
    const updated = await res.json();
    setTodos(prev => prev.map(t => (t.id === updated.id ? updated : t)));
  }

  async function deleteTodo(id) {
    await fetch(`/api/todos/${id}`, { method: 'DELETE' });
    setTodos(prev => prev.filter(t => t.id !== id));
  }

  function startEdit(todo) {
    setEditingId(todo.id);
    setEditTitle(todo.title);
  }

  async function saveEdit(todo) {
    const res = await fetch(`/api/todos/${todo.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ title: editTitle, completed: todo.completed }),
    });
    const updated = await res.json();
    setTodos(prev => prev.map(t => (t.id === updated.id ? updated : t)));
    setEditingId(null);
  }

  function handleKeyDown(e) {
    if (e.key === 'Enter') addTodo();
  }

  return (
    <div style={styles.container}>
      <div style={styles.header}>Todo List</div>
      <div style={styles.addRow}>
        <input
          style={styles.input}
          value={input}
          onChange={e => setInput(e.target.value)}
          onKeyDown={handleKeyDown}
          placeholder="What needs to be done?"
        />
        <button style={{ ...styles.btn, ...styles.addBtn }} onClick={addTodo}>Add</button>
      </div>

      {todos.length === 0 && <div style={styles.emptyMsg}>No todos yet. Add one above!</div>}

      {todos.map(todo => (
        <div key={todo.id} style={styles.todoItem}>
          <input
            type="checkbox"
            checked={todo.completed}
            onChange={() => toggleCompleted(todo)}
          />
          {editingId === todo.id ? (
            <>
              <input
                style={styles.editInput}
                value={editTitle}
                onChange={e => setEditTitle(e.target.value)}
                onKeyDown={e => e.key === 'Enter' && saveEdit(todo)}
                autoFocus
              />
              <button style={{ ...styles.btn, ...styles.saveBtn }} onClick={() => saveEdit(todo)}>Save</button>
              <button style={{ ...styles.btn, ...styles.cancelBtn }} onClick={() => setEditingId(null)}>Cancel</button>
            </>
          ) : (
            <>
              <span
                style={{ ...styles.todoTitle, ...(todo.completed ? styles.completed : {}) }}
                onDoubleClick={() => startEdit(todo)}
                title="Double-click to edit"
              >
                {todo.title}
              </span>
              <button style={{ ...styles.btn, ...styles.deleteBtn }} onClick={() => deleteTodo(todo.id)}>Delete</button>
            </>
          )}
        </div>
      ))}
    </div>
  );
}
