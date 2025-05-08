import { Container, Row, Col, Card } from 'react-bootstrap';

const HomePageView = () => {
    return (
        <div className="bg-warning-subtle min-vh-100 py-5">
            <Container>
                <h1 className="text-center mb-4">Tablica projektu</h1>
                <Row>
                    {/* Kolumna 1 */}
                    <Col md={4} className="mb-4">
                        <Card>
                            <div className="bg-success-subtle fw-bold text-success py-2 px-3">O projekcie</div>
                            <div className="bg-light border-bottom py-2 px-3">Projekt z programowania zwinnego</div>
                            <div className="bg-light border-bottom py-2 px-3">Dr. inż Damian Szczegielniak</div>
                            <div className="bg-light border-bottom py-2 px-3">Start 06.03.2025</div>
                            <div className="bg-light border-bottom py-2 px-3">Koniec 26.06.2025</div>
                        </Card>
                    </Col>

                    {/* Kolumna 2 */}
                    <Col md={4} className="mb-4">
                        <Card>
                            <div className="bg-success-subtle fw-bold text-success py-2 px-3 ">Uczestnicy projektu</div>
                            <a className="bg-light border-bottom py-2 px-3 d-block text-decoration-none text-dark" href={"https://github.com/Danrog303"}>Danrog303</a>
                            <a className="bg-light border-bottom py-2 px-3 d-block text-decoration-none text-dark" href={"https://github.com/Kyandi0"}>Kyandi0</a>
                            <a className="bg-light border-bottom py-2 px-3 d-block text-decoration-none text-dark" href={"https://github.com/IamKrytas"}>IamKrytas</a>
                            <a className="bg-light border-bottom py-2 px-3 d-block text-decoration-none text-dark" href={"https://github.com/LikeCiastka"}>LikeCiastka</a>
                            <a className="bg-light border-bottom py-2 px-3 d-block text-decoration-none text-dark" href={"https://github.com/Pawel-234"}>Pawel-234</a>                            
                        </Card>
                    </Col>

                    {/* Kolumna 3 */}
                    <Col md={4} className="mb-4">
                        <Card>
                            <div className="bg-success-subtle fw-bold text-success py-2 px-3">Moje zadania</div>
                            <div className="bg-light border-bottom py-2 px-3">PLACEHOLDER</div>
                            <div className="bg-light border-bottom py-2 px-3">PLACEHOLDER</div>
                            <div className="bg-light border-bottom py-2 px-3">PLACEHOLDER</div>
                            <div className="bg-light border-bottom py-2 px-3">PLACEHOLDER</div>
                            <div className="bg-light border-bottom py-2 px-3">PLACEHOLDER</div>
                        </Card>
                    </Col>
                </Row>
            </Container>
        </div>
    );
};

export default HomePageView;
