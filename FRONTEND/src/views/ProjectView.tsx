import { useState, useEffect } from 'react';
import { Table, Button, Modal, Form } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { toast } from "react-toastify";

import { UserRole } from "../models/auth/UserRole.ts";
import { getAllProjects, createProject, modifyProject, deleteProject } from '../controllers/projectController';
import { getAllStudents, getAllTeachers } from '../controllers/listUsersController.ts';
import { getAllTasks } from "../controllers/taskController.ts";

import { Project } from '../models/Project';
import { Teacher } from "../models/Teacher.ts";
import { Student } from "../models/Student.ts";
import { Task } from "../models/Task.ts";

function ProjectView() {
    const [projects, setProjects] = useState<Project[]>([]);
    const [tasks, setTasks] = useState<Task[]>([]);
    const [listTeachers, setListTeachers] = useState<Teacher[]>([]);
    const [listStudents, setListStudents] = useState<Student[]>([]);

    const [showModal, setShowModal] = useState(false);
    const [editingProject, setEditingProject] = useState<Project | null>(null);

    const userRole: UserRole = localStorage.getItem("accessRole") as UserRole;
    const navigate = useNavigate();

    const emptyProject: Project = {
        id: '',
        name: '',
        description: '',
        teacherId: '',
        fileIds: [],
        taskIds: [],
        studentIds: [],
        creationDate: new Date(),
        doneDate: new Date()
    };

    const [form, setForm] = useState({
        ...emptyProject,
        fileIds: '',
        studentIds: '',
        taskIds: '',
        creationDate: '',
        doneDate: ''
    });

    useEffect(() => {
        handleGetProjects();
        handleGetListAllTeachers();
        handleGetListAllStudents();
        handleGetTasks();
    }, []);

    const handleGetProjects = async () => {
        try {
            const response = await getAllProjects();
            setProjects(response);
        } catch (error) {
            toast("Błąd pobierania projektów: " + error);
        }
    };

    const handleGetListAllTeachers = async () => {
        try {
            const response = await getAllTeachers();
            setListTeachers(response);
        } catch (error) {
            toast("Błąd pobierania nauczycieli: " + error);
        }
    };

    const handleGetListAllStudents = async () => {
        try {
            const response = await getAllStudents();
            setListStudents(response);
        } catch (error) {
            toast("Błąd pobierania studentów: " + error);
        }
    };

    const handleGetTasks = async () => {
        try {
            const response = await getAllTasks();
            setTasks(response);
        } catch (error) {
            toast("Błąd pobierania zadań: " + error);
        }
    };

    const handleDelete = async (id: string) => {
        try {
            await deleteProject(id);
            handleGetProjects();
        } catch (error) {
            toast("Błąd usuwania projektu: " + error);
        }
    };

    const handleEdit = (project: Project) => {
        setEditingProject(project);
        setForm({
            ...project,
            fileIds: (project.fileIds ?? []).join(', '),
            studentIds: (project.studentIds ?? []).join(', '),
            taskIds: (project.taskIds ?? []).join(', '),
            creationDate: project.creationDate ? new Date(project.creationDate).toISOString().slice(0, 10) : '',
            doneDate: project.doneDate ? new Date(project.doneDate).toISOString().slice(0, 10) : '',
        });
        setShowModal(true);
    };

    const handleCreate = () => {
        setEditingProject(null);
        setForm({
            ...emptyProject,
            fileIds: '',
            studentIds: '',
            taskIds: '',
            creationDate: '',
            doneDate: ''
        });
        setShowModal(true);
    };

    const handleSave = async () => {
        const payload: Project = {
            ...form,
            fileIds: form.fileIds ? form.fileIds.split(',').map(s => s.trim()) : [],
            studentIds: form.studentIds ? form.studentIds.split(',').map(s => s.trim()) : [],
            taskIds: form.taskIds ? form.taskIds.split(',').map(s => s.trim()) : [],
            creationDate: form.creationDate ? new Date(form.creationDate) : new Date(),
            doneDate: form.doneDate ? new Date(form.doneDate) : new Date(),
        };

        try {
            if (editingProject) {
                await modifyProject(payload);
            } else {
                await createProject(payload);
            }
            setShowModal(false);
            handleGetProjects();
        } catch (error) {
            toast("Błąd zapisu projektu: " + error);
        }
    };

    const handleInputChange = (field: string, value: string) => {
        setForm(prev => ({
            ...prev,
            [field]: value
        }));
    };

    return (
        <div className="container mt-4">
            <h2>Zarządzanie Projektami</h2>

            {userRole === "TEACHER" && (
                <Button variant="primary" onClick={handleCreate} className="mb-3">Dodaj Projekt</Button>
            )}

            <Table responsive striped bordered hover>
                <thead>
                    <tr>
                        <th>Nazwa</th>
                        <th>Opis</th>
                        <th>Nauczyciel</th>
                        <th>Studenci</th>
                        <th>Zadania</th>
                        <th>Pliki</th>
                        <th>Data Utworzenia</th>
                        <th>Data Zakończenia</th>
                        <th>Akcje</th>
                    </tr>
                </thead>
                <tbody>
                    {projects.length === 0 ? (
                        <tr>
                            <td colSpan={9} className="text-center">Brak projektów do wyświetlenia.</td>
                        </tr>
                    ) : (
                        projects.map((project) => (
                            <tr key={project.id}>
                                <td>{project.name}</td>
                                <td>{project.description}</td>
                                <td>
                                    {(() => {
                                        const teacher = listTeachers.find(t => t.id === project.teacherId);
                                        return teacher ? `${teacher.name} ${teacher.surname}` : '';
                                    })()}
                                </td>
                                <td>
                                    {(project.studentIds ?? []).map(id => {
                                        const student = listStudents.find(s => s.id === id);
                                        return student ? `${student.name} ${student.surname}` : '';
                                    }).join(', ')}
                                </td>
                                <td>
                                    {(project.taskIds ?? []).map(id => {
                                        const task = tasks.find(t => t.id === id);
                                        return task ? task.name : '';
                                    }).join(', ')}
                                </td>
                                <td>{(project.fileIds ?? []).join(', ')}</td>
                                <td>{new Date(project.creationDate).toISOString().slice(0, 10)}</td>
                                <td>{new Date(project.doneDate).toISOString().slice(0, 10)}</td>
                                <td>
                                    {userRole === "TEACHER" && (
                                        <Button size="sm" onClick={() => handleEdit(project)} className="me-2">Edytuj</Button>
                                    )}
                                    <Button size="sm" variant="warning" onClick={() => navigate(`/project/${project.id}`)} className="me-2">Zobacz</Button>
                                    {userRole === "TEACHER" && (
                                        <Button size="sm" variant="danger" onClick={() => handleDelete(project.id)}>Usuń</Button>
                                    )}
                                </td>
                            </tr>
                        ))
                    )}
                </tbody>
            </Table>

            <Modal show={showModal} onHide={() => setShowModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>{editingProject ? 'Edytuj Projekt' : 'Nowy Projekt'}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-2">
                            <Form.Label>Nazwa</Form.Label>
                            <Form.Control
                                type="text"
                                value={form.name}
                                onChange={(e) => handleInputChange('name', e.target.value)}
                            />
                        </Form.Group>
                        <Form.Group className="mb-2">
                            <Form.Label>Opis</Form.Label>
                            <Form.Control
                                type="text"
                                value={form.description}
                                onChange={(e) => handleInputChange('description', e.target.value)}
                            />
                        </Form.Group>
                        <Form.Group className="mb-2">
                            <Form.Label>Wybierz Nauczyciela</Form.Label>
                            <Form.Select
                                value={form.teacherId}
                                onChange={(e) => handleInputChange('teacherId', e.target.value)}
                            >
                                <option value="">Wybierz Nauczyciela</option>
                                {listTeachers.map((teacher) => (
                                    <option key={teacher.id} value={teacher.id}>
                                        {teacher.name} {teacher.surname}
                                    </option>
                                ))}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-2">
                            <Form.Label>Wybierz Studentów</Form.Label>
                            <Form.Select
                                multiple
                                value={form.studentIds ? form.studentIds.split(',').map(id => id.trim()) : []}
                                onChange={(e) => {
                                    const selectedOptions = Array.from(e.target.selectedOptions, option => option.value);
                                    handleInputChange('studentIds', selectedOptions.join(','));
                                }}
                            >
                                {listStudents.map((student) => (
                                    <option key={student.id} value={student.id}>
                                        {student.name} {student.surname}
                                    </option>
                                ))}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-2">
                            <Form.Label>Wybierz Zadania</Form.Label>
                            <Form.Select
                                multiple
                                value={form.taskIds ? form.taskIds.split(',').map(id => id.trim()) : []}
                                onChange={(e) => {
                                    const selectedOptions = Array.from(e.target.selectedOptions, option => option.value);
                                    handleInputChange('taskIds', selectedOptions.join(','));
                                }}
                            >
                                {tasks.map((task) => (
                                    <option key={task.id} value={task.id}>
                                        {task.name}
                                    </option>
                                ))}
                            </Form.Select>
                        </Form.Group>
                        <Form.Group className="mb-2">
                            <Form.Label>ID Plików (oddzielone przecinkami)</Form.Label>
                            <Form.Control
                                type="text"
                                value={form.fileIds}
                                onChange={(e) => handleInputChange('fileIds', e.target.value)}
                            />
                        </Form.Group>
                        <Form.Group className="mb-2">
                            <Form.Label>Data Utworzenia</Form.Label>
                            <Form.Control
                                type="date"
                                value={form.creationDate}
                                onChange={(e) => handleInputChange('creationDate', e.target.value)}
                            />
                        </Form.Group>
                        <Form.Group className="mb-2">
                            <Form.Label>Data Zakończenia</Form.Label>
                            <Form.Control
                                type="date"
                                value={form.doneDate}
                                onChange={(e) => handleInputChange('doneDate', e.target.value)}
                            />
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowModal(false)}>Anuluj</Button>
                    <Button variant="success" onClick={handleSave}>Zapisz</Button>
                </Modal.Footer>
            </Modal>
        </div>
    );
}

export default ProjectView;