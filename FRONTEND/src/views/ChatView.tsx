import React, { useEffect, useRef, useState } from 'react';
import SockJS from 'sockjs-client';
import { Client, IMessage, StompSubscription } from '@stomp/stompjs';
import { Container, Form, Button, Card, ListGroup } from 'react-bootstrap';
import {toast} from "react-toastify";

const WS_URL = 'http://localhost:8080/api/v1/ws';

type Message = {
    sender: string;
    content: string;
};

const ChatComponent: React.FC = () => {
    const [messages, setMessages] = useState<Message[]>([]);
    const [userName, setUsername] = useState('');
    const [input, setInput] = useState('');

    const stompClientRef = useRef<Client | null>(null);
    const subscriptionRef = useRef<StompSubscription | null>(null);

    useEffect(() => {
        const sub = sessionStorage.getItem('refreshSub') as string;
        const name = sub.split('@')[0];
        setUsername(name);

        const socket = new SockJS(WS_URL);
        const stompClient = new Client({
            webSocketFactory: () => socket,
            reconnectDelay: 5000,
            debug: (str) => console.log('STOMP: ', str),
        });

        stompClient.onConnect = () => {
            console.log('Connected to STOMP');
            subscriptionRef.current = stompClient.subscribe('/topic/messages', (messageOutput: IMessage) => {
                const msg: Message = JSON.parse(messageOutput.body);
                console.debug('New STOMP message:', msg.content);
                setMessages(prev => [...prev, msg]);
            });
        };

        stompClient.onStompError = (frame) => {
            toast('STOMP error: ' + frame.headers['message']);
            toast('Details: ' + frame.body);
        };

        stompClient.activate();
        stompClientRef.current = stompClient;

        return () => {
            subscriptionRef.current?.unsubscribe();
            stompClient.deactivate();
        };
    }, []);

    const sendMessage = () => {
        if (stompClientRef.current && stompClientRef.current.connected && input.trim()) {
            stompClientRef.current.publish({
                destination: '/app/send',
                body: JSON.stringify({
                    sender: userName,
                    content: input
                }),
            });
            setInput('');
        }
    };

    return (
        <Container className="mt-4">
            <h2 className="text-center mb-4">Chat</h2>
            <Card>
                <Card.Header>Wiadomości</Card.Header>
                <Card.Body style={{ maxHeight: '300px', overflowY: 'auto' }}>
                    <ListGroup variant="flush">
                        {messages.map((msg, index) => (
                            <ListGroup.Item key={index}>
                                <strong>{msg.sender}:</strong> {msg.content}
                            </ListGroup.Item>
                        ))}
                    </ListGroup>
                </Card.Body>
                <Card.Footer>
                    <Form
                        onSubmit={(e) => {
                            e.preventDefault();
                            sendMessage();
                        }}
                    >
                        <Form.Group className="d-flex gap-2">
                            <Form.Control
                                type="text"
                                value={input}
                                placeholder="Wpisz twoją wiadomość"
                                onChange={(e) => setInput(e.target.value)}
                            />
                            <Button variant="primary" onClick={sendMessage}>
                                Wyślij
                            </Button>
                        </Form.Group>
                    </Form>
                </Card.Footer>
            </Card>
        </Container>
    );
};

export default ChatComponent;
