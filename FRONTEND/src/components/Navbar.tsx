import { Navbar, Nav, Container } from 'react-bootstrap';
import { logoutUserService } from '../services/userService';

const NavigationBar = () => {
  return (
    <Navbar bg="success" variant="dark" expand="lg">
      <Container>
        <Navbar.Brand href="#">Moje Projekty</Navbar.Brand>
        <Navbar.Toggle aria-controls="project-navbar-nav" />
        <Navbar.Collapse id="project-navbar-nav">
          <Nav className="ms-auto">
            <Nav.Link href="#projekty">Projekty</Nav.Link>
            <Nav.Link href="#uczestnicy">Uczestnicy</Nav.Link>
            <Nav.Link href="#zadania">Zadania</Nav.Link>
            <Nav.Link href="#profil">Profil</Nav.Link>
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
