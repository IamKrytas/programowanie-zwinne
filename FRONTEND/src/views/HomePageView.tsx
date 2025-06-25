import {Container, Row, Card, Button} from 'react-bootstrap';
import {useEffect, useState} from "react";
import {getStats} from "../controllers/statsController.ts";
import Stats from "../models/Stats.ts";
import {UserRole} from "../models/auth/UserRole.ts";
import {Link} from "react-router-dom";

const HomePageView = () => {
    const [stats, setStats] = useState<Stats | null>(null);
    const userRole = localStorage.getItem("accessRole") as UserRole;

    useEffect(() => {
        (async () => {
            setStats(await getStats());
        })();
    }, []);

    return (
        <div className="bg-warning-subtle min-vh-100 py-5">
            <Container>
                <h1 className="text-center mb-4">Globalne statystyki aplikacji</h1>
                <Row>
                    {stats === null && "..."}
                    {stats !== null && <p>
                        <Card className="mb-3 text-center">
                            <Card.Body>
                                <Card.Text>
                                    <strong>Liczba projektów</strong>: {(stats as Stats).totalProjects}<br/>
                                    <strong>Liczba zadań</strong>: {(stats as Stats).totalTasks}<br/>
                                    <strong>Liczba nauczycieli</strong>: {(stats as Stats).totalTeachers}<br/>
                                    <strong>Liczba studentów</strong>: {(stats as Stats).totalStudents}
                                </Card.Text>
                            </Card.Body>
                        </Card>
                    </p>}
                </Row>

                <div className={"text-center"}>
                    {userRole !== "ADMIN" && <Link to={"/projects"}><Button variant="success" className="mt-3 m-1">Projekty</Button></Link>}
                    {userRole !== "ADMIN" && <Link to={"/tasks"}><Button variant="success" className="mt-3 m-1">Zadania</Button></Link>}
                    <Link to={"/chat"}><Button variant="success" className="mt-3 m-1">Chat</Button></Link>
                </div>
            </Container>
        </div>
    );
};

export default HomePageView;
