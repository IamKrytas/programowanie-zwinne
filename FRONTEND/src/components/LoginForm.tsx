// imports
import { useState } from 'react';
import { Button, Card, Col, Container, Form, Row } from 'react-bootstrap';
import { User } from '../models/User';
import { loginUser } from '../controllers/userController';

const LoginForm = () => {

    const [formData, setFormData] = useState<User>({
        email: '',
        password: '',
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        console.log(formData);

        try {
            await loginUser(formData);
            window.location.href = '/home';
        }
        catch (error) {
            console.error(error);
        }
    };


    return (
        <Container className="vh-100 justify-content-center align-items-center mt-3">
            <Row className="justify-content-center h-100">
                <Col xs={12} md={6} lg={4}>
                    <Card className="shadow-lg border-0">
                        <Card.Body className="p-4">
                            <h3 className="text-center mb-4 text-primary">Logowanie</h3>
                            <h6 className="text-center mb-4 text-muted">Wprowadź swoje dane, aby przejść do aplikacji</h6>
                            <Form onSubmit={handleSubmit}>
                                <Form.Group className="mb-3" controlId="formEmail">
                                    <Form.Label>Email</Form.Label>
                                    <Form.Control
                                        type="text"
                                        name="email"
                                        placeholder="Wpisz adres e-mail"
                                        value={formData.email}
                                        onChange={handleChange}
                                        required
                                    />
                                </Form.Group>

                                <Form.Group className="mb-3" controlId="formPassword">
                                    <Form.Label>Hasło</Form.Label>
                                    <Form.Control
                                        type="password"
                                        name="password"
                                        placeholder="Wpisz hasło"
                                        value={formData.password}
                                        onChange={handleChange}
                                        required
                                    />
                                </Form.Group>

                                <Button
                                    variant="primary"
                                    type="submit"
                                    className="w-100 mb-2"
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
