import { Navbar, Nav, Container } from 'react-bootstrap';
import { logoutUserService } from '../services/userService';
import { Link } from "react-router-dom";
import {UserRole} from "../models/auth/UserRole.ts";

const NavigationBar = () => {
  const role = localStorage.getItem('accessRole') as UserRole;
  const userName: string = localStorage.getItem('accessSub')?.split('@')[0] ?? 'Anonymous';

  function translateRole(role: UserRole): string {
    switch (role) {
      case UserRole.ADMIN:
        return 'administrator';
      case UserRole.STUDENT:
        return 'student';
      case UserRole.TEACHER:
        return 'nauczyciel';
      default:
        return 'nieznana rola';
    }
  }

  return (
    <Navbar bg="success" variant="dark" expand="lg">
      <Container>
        <Navbar.Brand as={Link} to="/">Moje Projekty</Navbar.Brand>
        <Navbar.Text className="ms-3">Witaj, {userName}! Twoja rola to {translateRole(role)}.</Navbar.Text>
        <Navbar.Toggle aria-controls="project-navbar-nav" />
        <Navbar.Collapse id="project-navbar-nav">
          <Nav className="ms-auto">
            {role !== "ADMIN" && <Nav.Link as={Link} to="/projects">Projekty</Nav.Link>}
            {role !== "ADMIN" && <Nav.Link as={Link} to="/tasks">Zadania</Nav.Link>}
            {/*<Nav.Link href="#uczestnicy">Uczestnicy</Nav.Link>*/}
            {/*<Nav.Link href="#profil">Profil</Nav.Link>*/}
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
