// imports
import {useNavigate} from "react-router-dom";
import { useState } from 'react';
import { Button, Card, Col, Container, Form, Row } from 'react-bootstrap';
import { Student } from '../models/Student';
import { registerUser } from '../controllers/userController';

const RegisterForm = () => {

    const navigate = useNavigate();
    const [confirmPassword, setConfirmPassword] = useState('');
    const [formData, setFormData] = useState<Student>({
        name: '',
        surname: '',
        email: '',
        password: '',
        stationary: null,
    });

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        console.log(formData);

        if (formData.password !== confirmPassword) {
            window.alert('Hasła nie są takie same!');
            return;
        }
        try {
            const message = await registerUser(formData);
            console.log(message);
            navigate('/logowanie');
        }
        catch (error) {
            console.error(error);
        }
    };


    return (
        <Container className="justify-content-center align-items-center mt-3">
            <Row className="justify-content-center h-100">
                <Col xs={12} md={8} lg={6}>
                    <Card className="shadow-lg border-0">
                        <Card.Body className="p-4">
                            <h3 className="text-center mb-3 text-primary">Rejestracja</h3>
                            <h6 className="text-center mb-3 text-muted"> Utwórz konto, aby korzystać z aplikacji</h6>
                            <Form onSubmit={handleSubmit}>
                                <Form.Group className="mb-3" controlId="formName">
                                    <Form.Label>Podaj imię</Form.Label>
                                    <Form.Control
                                        type="text"
                                        name="name"
                                        placeholder="Wprowadź imię"
                                        value={formData.name}
                                        onChange={handleChange}
                                        required
                                    />
                                </Form.Group>
                                <Form.Group className="mb-3" controlId="formSurname">
                                    <Form.Label>Podaj nazwisko</Form.Label>
                                    <Form.Control
                                        type="text"
                                        name="surname"
                                        placeholder="Wprowadź nazwisko"
                                        value={formData.surname}
                                        onChange={handleChange}
                                        required
                                    />
                                </Form.Group>

                                <Form.Group className="mb-3" controlId="formEmail">
                                    <Form.Label>Email</Form.Label>
                                    <Form.Control
                                        type="email"
                                        name="email"
                                        placeholder="Wprowadź email"
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
                                        placeholder="Wprowadź hasło"
                                        value={formData.password}
                                        onChange={handleChange}
                                        required
                                    />
                                </Form.Group>

                                <Form.Group className="mb-3" controlId="formConfirmPassword">
                                    <Form.Label>Powtórz hasło</Form.Label>
                                    <Form.Control
                                        type="password"
                                        name='confirmPassword'
                                        placeholder="Powtórz hasło"
                                        value={confirmPassword}
                                        onChange={(e) => setConfirmPassword(e.target.value)}
                                        required
                                    />
                                </Form.Group>

                                <Form.Group className="mb-3 text-center" controlId="formStationary">
                                    <Form.Label>Podaj sposób nauki</Form.Label>
                                    <div className="d-flex justify-content-center">
                                        <Form.Check
                                            type="radio"
                                            name="stationary"
                                            label="Stacjonarnie"
                                            value="true"
                                            checked={formData.stationary === true}
                                            className="me-3"
                                            required
                                            onChange={(e) => setFormData({ ...formData, stationary: e.target.value === 'true' })}
                                        />
                                        <Form.Check
                                            type="radio"
                                            name="stationary"
                                            label="Niestacjonarnie"
                                            value="false"
                                            checked={formData.stationary === false}
                                            onChange={(e) => setFormData({ ...formData, stationary: e.target.value === 'true' })}
                                        />
                                    </div>
                                </Form.Group>
                                <Button
                                    variant="primary"
                                    type="submit"
                                    className="w-100 mt-3"
                                >
                                    Zarejestruj się
                                </Button>
                            </Form>
                        </Card.Body>
                        <Card.Footer className="bg-white text-center py-3">
                            <p className="mb-0">
                                Masz już konto?{' '}
                                <a href="/logowanie" className="text-primary">
                                    Zaloguj się
                                </a>
                            </p>
                        </Card.Footer>
                    </Card>
                </Col>
            </Row>
        </Container >
    );
};

export default RegisterForm;