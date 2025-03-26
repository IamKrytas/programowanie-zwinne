// imports
import { useState } from 'react';
import { Button, Card, Col, Container, Form, Row } from 'react-bootstrap';

const LoginForm = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    ``
    return (
        <Container className="vh-100 justify-content-center align-items-center mt-3">
            <Row className="justify-content-center h-100">
                <Col xs={12} md={6} lg={4}>
                    <Card className="shadow-lg border-0">
                        <Card.Body className="p-4">
                            <h3 className="text-center mb-4 text-primary">Logowanie</h3>
                            <h6 className="text-center mb-4 text-muted">Wprowadź swoje dane, aby przejść do aplikacji</h6>
                            <Form>
                                <Form.Group className="mb-3" controlId="formEmail">
                                    <Form.Label>Email</Form.Label>
                                    <Form.Control
                                        type="email"
                                        placeholder="Wpisz adres e-mail"
                                        name="email"
                                        value={email}
                                        onChange={(e) => setEmail(e.target.value)}
                                        required
                                    />
                                </Form.Group>

                                <Form.Group className="mb-3" controlId="formPassword">
                                    <Form.Label>Hasło</Form.Label>
                                    <Form.Control
                                        type="password"
                                        placeholder="Wprowadź hasło"
                                        name="password"
                                        value={password}
                                        onChange={(e) => setPassword(e.target.value)}
                                        required
                                    />
                                </Form.Group>

                                <Button
                                    variant="primary"
                                    type="button"
                                    className="w-100 mb-2"
                                    onClick={() => console.log('Zaloguj')}
                                >
                                    Zaloguj się
                                </Button>
                            </Form>
                        </Card.Body>
                        <Card.Footer className="bg-white text-center py-3">
                            <p className="mb-0">
                                Nie masz konta?{' '}
                                <a href="/rejestracja" className="text-primary">
                                    Zarejestruj się
                                </a>
                            </p>
                        </Card.Footer>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
};

export default LoginForm;
