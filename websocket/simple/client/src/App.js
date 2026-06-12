import React, { useState, useEffect, useRef } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import './App.css';

export default function App() {
  const [connected, setConnected] = useState(false);
  const [username, setUsername] = useState('');
  const [joined, setJoined] = useState(false);
  const [message, setMessage] = useState('');
  const [messages, setMessages] = useState([]);
  const clientRef = useRef(null);
  const bottomRef = useRef(null);

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  function connect(name) {
    const client = new Client({
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
      onConnect: () => {
        setConnected(true);
        client.subscribe('/topic/public', (frame) => {
          const msg = JSON.parse(frame.body);
          setMessages((prev) => [...prev, msg]);
        });
        // announce join
        client.publish({
          destination: '/app/chat.join',
          body: JSON.stringify({ sender: name, content: '' }),
        });
      },
      onDisconnect: () => setConnected(false),
    });
    client.activate();
    clientRef.current = client;
  }

  function handleJoin(e) {
    e.preventDefault();
    if (!username.trim()) return;
    setJoined(true);
    connect(username.trim());
  }

  function handleSend(e) {
    e.preventDefault();
    if (!message.trim() || !clientRef.current?.connected) return;
    clientRef.current.publish({
      destination: '/app/chat.send',
      body: JSON.stringify({ sender: username, content: message.trim() }),
    });
    setMessage('');
  }

  if (!joined) {
    return (
      <div className="join-screen">
        <div className="join-card">
          <h1>WebSocket Chat</h1>
          <p className="subtitle">Spring Boot + React + STOMP</p>
          <form onSubmit={handleJoin}>
            <input
              autoFocus
              placeholder="Enter your name…"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
            <button type="submit">Join Chat</button>
          </form>
        </div>
      </div>
    );
  }

  return (
    <div className="chat-screen">
      <header>
        <span>WebSocket Chat</span>
        <span className={`status ${connected ? 'online' : 'offline'}`}>
          {connected ? '● Connected' : '○ Disconnected'}
        </span>
      </header>

      <div className="messages">
        {messages.map((msg, i) => {
          const isMe = msg.sender === username;
          const isSystem = msg.content?.includes('joined the chat');
          if (isSystem) {
            return (
              <div key={i} className="system-msg">{msg.content} · {msg.timestamp}</div>
            );
          }
          return (
            <div key={i} className={`bubble-row ${isMe ? 'me' : 'other'}`}>
              {!isMe && <span className="sender-name">{msg.sender}</span>}
              <div className="bubble">
                <span className="text">{msg.content}</span>
                <span className="time">{msg.timestamp}</span>
              </div>
            </div>
          );
        })}
        <div ref={bottomRef} />
      </div>

      <form className="input-bar" onSubmit={handleSend}>
        <input
          autoFocus
          placeholder="Type a message…"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          disabled={!connected}
        />
        <button type="submit" disabled={!connected || !message.trim()}>
          Send
        </button>
      </form>
    </div>
  );
}
