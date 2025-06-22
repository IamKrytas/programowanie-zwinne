import { Navbar, Nav, Container } from 'react-bootstrap';
import { logoutUserService } from '../services/userService';
import { Link } from "react-router-dom";

const NavigationBar = () => {
  const role = sessionStorage.getItem('accessRole');

  return (
    <Navbar bg="success" variant="dark" expand="lg">
      <Container>
        <Navbar.Brand as={Link} to="/">Moje Projekty</Navbar.Brand>
        <Navbar.Toggle aria-controls="project-navbar-nav" />
        <Navbar.Collapse id="project-navbar-nav">
          <Nav className="ms-auto">
            <Nav.Link as={Link} to="/projects">Projekty</Nav.Link>
            <Nav.Link as={Link} to="/tasks">Zadania</Nav.Link>
            <Nav.Link href="#uczestnicy">Uczestnicy</Nav.Link>
            <Nav.Link href="#profil">Profil</Nav.Link>
            <Nav.Link as={Link} to="/chat">Chat</Nav.Link>
            {role === 'ADMIN' && (
              <Nav.Link as={Link} to="/admin">Panel Admina</Nav.Link>
            )}
            <Nav.Link onClick={logoutUserService}>
              Wyloguj
            </Nav.Link>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default NavigationBar;
