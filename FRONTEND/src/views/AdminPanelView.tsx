import React, { useEffect, useState } from 'react';
import { Container, Tabs, Tab, Table, Button, Form, Modal } from 'react-bootstrap';
import { Student } from '../models/Student';
import { Teacher } from '../models/Teacher';
import { getAllStudents, createStudent, modifyStudent, deleteStudentById } from '../controllers/adminStudentController';
import { getAllTeachers, createTeacher, modifyTeacher, deleteTeacherById } from '../controllers/adminTeacherController';
import {toast} from "react-toastify";

function AdminPanelView() {
  const [students, setStudents] = useState<Student[]>([]);
  const [teachers, setTeachers] = useState<Teacher[]>([]);
  const [showModal, setShowModal] = useState(false);
  const [editing, setEditing] = useState<{ mode: 'student' | 'teacher'; id?: string } | null>(null);
  const [formData, setFormData] = useState<Partial<Student & Teacher>>({});

  useEffect(() => {
    handleGetStudents();
    handleGetTeachers();
  }, []);

  const emptyStudent: Student = {
    id: '',
    name: '',
    surname: '',
    email: '',
    password: '',
    stationary: false,
  };

  const emptyTeacher: Teacher = {
    id: '',
    name: '',
    surname: '',
    email: '',
    password: '',
  };

  const handleGetStudents = async () => {
    try {
      const response = await getAllStudents();
      setStudents(response);
    } catch (error) {
      toast('Error fetching students: ' + error);
    }
  };

  const handleGetTeachers = async () => {
    try {
      const response = await getAllTeachers();
      setTeachers(response);
    } catch (error) {
      toast('Error fetching teachers: ' + error);
    }
  };

  const handleCreateNewStudent = async (student: Student) => {
    await createStudent(student);
    await handleGetStudents();
  };

  const handleCreateNewTeacher = async (teacher: Teacher) => {
    await createTeacher(teacher);
    await handleGetTeachers();
  };

  const handleUpdateStudent = async (student: Student) => {
    await modifyStudent(student);
    await handleGetStudents();
    setEditing(null);
  };

  const handleUpdateTeacher = async (teacher: Teacher) => {
    await modifyTeacher(teacher);
    await handleGetTeachers();
    setEditing(null);
  };

  const handleDeleteStudent = async (id: string) => {
    await deleteStudentById(id);
    await handleGetStudents();
  };

  const handleDeleteTeacher = async (id: string) => {
    await deleteTeacherById(id);
    await handleGetTeachers();
  };

  const handleEdit = (type: 'student' | 'teacher', id: string) => {
    const data = type === 'student'
      ? students.find(s => s.id === id)
      : teachers.find(t => t.id === id);

    if (!data) {
      toast(`${type} with ID ${id} not found`);
      return;
    }

    setFormData(data);
    setEditing({ mode: type, id });
    setShowModal(true);
  };

  const handleAddNew = (type: 'student' | 'teacher') => {
    setFormData(type === 'student' ? emptyStudent : emptyTeacher);
    setEditing({ mode: type });
    setShowModal(true);
  };

  const handleFormChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, type, value, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value,
    }));
  };

  const handleSubmit = async () => {
    if (!editing) return;

    const isStudent = editing.mode === 'student';

    try {
      if (editing.id) {
        isStudent
          ? await handleUpdateStudent(formData as Student)
          : await handleUpdateTeacher(formData as Teacher);
      } else {
        isStudent
          ? await handleCreateNewStudent(formData as Student)
          : await handleCreateNewTeacher(formData as Teacher);
      }

      setShowModal(false);
      setFormData({});
      setEditing(null);
    } catch (error) {
      toast('Error saving data: ' + error);
    }
  };

  const renderTable = (data: Student[] | Teacher[], type: 'student' | 'teacher') => (
    <>
      <Button onClick={() => handleAddNew(type)} className="mt-3 mb-3">
        Dodaj {type === 'student' ? 'Studenta' : 'Nauczyciela'}
      </Button>
      <Table responsive striped bordered hover>
        <thead>
          <tr>
            <th>Name</th>
            <th>Surname</th>
            <th>Password</th>
            <th>Email</th>
            {type === 'student' && <th>Stationary</th>}
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {data.map(item => (
            <tr key={item.id}>
              <td>{item.name}</td>
              <td>{item.surname}</td>
              <td>{item.email}</td>
              <td>{item.password}</td>
              {type === 'student' && (
                <td>
                  {(item as Student).stationary ? <span>Tak</span> : <span>Nie</span>}
                </td>
              )}
              <td>
                <Button
                  variant="primary"
                  size="sm"
                  onClick={() => handleEdit(type, item.id!)}
                  className="me-2"
                >
                  Edytuj
                </Button>
                <Button
                  variant="danger"
                  size="sm"
                  onClick={() =>
                    type === 'student'
                      ? handleDeleteStudent(item.id!)
                      : handleDeleteTeacher(item.id!)
                  }
                >
                  Usuń
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </>
  );

  return (
    <Container className="mt-4">
      <Tabs defaultActiveKey="students">
        <Tab eventKey="students" title="Students">
          {renderTable(students, 'student')}
        </Tab>
        <Tab eventKey="teachers" title="Teachers">
          {renderTable(teachers, 'teacher')}
        </Tab>
      </Tabs>

      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>{editing?.id ? 'Edit' : 'Add'} {editing?.mode}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form>
            <Form.Group controlId="formName" className="mb-2">
              <Form.Label>Name</Form.Label>
              <Form.Control
                type="text"
                name="name"
                value={formData.name || ''}
                onChange={handleFormChange}
              />
            </Form.Group>
            <Form.Group controlId="formSurname" className="mb-2">
              <Form.Label>Surname</Form.Label>
              <Form.Control
                type="text"
                name="surname"
                value={formData.surname || ''}
                onChange={handleFormChange}
              />
            </Form.Group>
            <Form.Group controlId="formEmail" className="mb-2">
              <Form.Label>Email</Form.Label>
              <Form.Control
                type="email"
                name="email"
                value={formData.email || ''}
                onChange={handleFormChange}
              />
            </Form.Group>
            <Form.Group controlId="formPassword" className="mb-2">
              <Form.Label>Password</Form.Label>
              <Form.Control
                type="password"
                name="password"
                value={formData.password || ''}
                onChange={handleFormChange}
              />
            </Form.Group>
            {editing?.mode === 'student' && (
              <Form.Group controlId="formStationary" className="mb-2">
                <Form.Check
                  type="checkbox"
                  label="Stationary"
                  name="stationary"
                  checked={!!formData.stationary}
                  onChange={handleFormChange}
                />
              </Form.Group>
            )}
          </Form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>Cancel</Button>
          <Button variant="primary" onClick={handleSubmit}>Save</Button>
        </Modal.Footer>
      </Modal>
    </Container>
  );
}

export default AdminPanelView;
