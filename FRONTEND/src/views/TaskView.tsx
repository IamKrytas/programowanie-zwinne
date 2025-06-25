import { UserRole } from "../models/auth/UserRole.ts";
import { useState, useEffect } from 'react';
import { getAllTasks, createTask, modifyTask, deleteTask } from '../controllers/taskController';
import { getAllStudents, getAllTeachers } from '../controllers/listUsersController.ts';
import { getAllProjects } from '../controllers/projectController.ts';
import { Table, Button, Modal, Form, Container } from 'react-bootstrap';
import { Task } from '../models/Task';
import { Student } from '../models/Student.ts';
import { Teacher } from '../models/Teacher.ts';
import { Project } from '../models/Project.ts';
import { toast } from "react-toastify";

function TaskView() {
    const [tasks, setTasks] = useState<Task[]>([]);
    const [showModal, setShowModal] = useState(false);
    const [editingTask, setEditingTask] = useState<Task | null>(null);
    const [listTeachers, setListTeachers] = useState<Teacher[]>([]);
    const [listStudents, setListStudents] = useState<Student[]>([]);
    const [projects, setProjects] = useState<Project[]>([]);

    const userRole: UserRole = localStorage.getItem("accessRole") as UserRole;

    const emptyTask: Task = {
        id: '',
        name: '',
        description: '',
        priority: 1,
        assignedStudentId: 0,
        projectId: '',
        teacherId: '',
        fileIds: [],
        creationDate: new Date(),
        doneDate: new Date()
    };

    const [form, setForm] = useState<any>({
        ...emptyTask,
        fileIds: '',
        creationDate: '',
        doneDate: ''
    });

    useEffect(() => {
        handleGetTasks();
        handleGetProjects();
        handleGetListAllTeachers();
        handleGetListAllStudents();
    }, []);

    const handleGetProjects = async () => {
        try {
            const response = await getAllProjects();
            setProjects(response);
        } catch (error) {
            toast("Błąd pobierania projektów: " + error);
        }
    };

    const handleGetTasks = async () => {
        try {
            const response = await getAllTasks();
            setTasks(response);
        } catch (error) {
            toast("Error fetching tasks: " + error);
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

    const handleDelete = async (id: string) => {
        try {
            await deleteTask(id);
            console.log("Task deleted:", id);
            handleGetTasks();
        } catch (error) {
            toast("Error deleting task: " + error);
        }
    };

    const handleEdit = (task: Task) => {
        setEditingTask(task);
        setForm({
            ...task,
            fileIds: task.fileIds.join(','),
            creationDate: task.creationDate ? new Date(task.creationDate).toISOString().slice(0, 10) : '',
            doneDate: task.doneDate ? new Date(task.doneDate).toISOString().slice(0, 10) : '',
        });
        setShowModal(true);
    };

    const handleCreate = () => {
        setEditingTask(null);
        setForm({
            ...emptyTask,
            fileIds: '',
            doneDate: '',
            creationDate: ''
        });
        setShowModal(true);
    };

    const handleSave = async () => {
        const payload: Task = {
            ...form,
            fileIds: form.fileIds.split(',').map((id: string) => id.trim()),
            assignedStudentId: parseInt(form.assignedStudentId),
            priority: parseInt(form.priority),
            doneDate: new Date(form.doneDate),
            creationDate: new Date(form.creationDate)
        };

        try {
            if (editingTask) {
                await modifyTask(payload);
            } else {
                await createTask(payload);
            }
            setShowModal(false);
            handleGetTasks();
        } catch (error) {
            toast("Error saving task: " + error);
        }
    };

    return (
        <Container className="mt-4">
            <h2>Zarządzanie Zadaniami</h2>
            {userRole === "TEACHER" && <Button variant="primary" onClick={handleCreate} className="mb-3">Dodaj Zadanie</Button>}
            <Table responsive striped bordered hover>
                <thead>
                    <tr>
                        <th>Nazwa</th>
                        <th>Opis</th>
                        <th>Nazwa projektu</th>
                        <th>Student</th>
                        <th>Nauczyciel</th>
                        <th>Pliki</th>
                        <th>Priorytet</th>
                        <th>Utworzono</th>
                        <th>Zakończono</th>
                        <th>Akcje</th>
                    </tr>
                </thead>
                <tbody>
                    {tasks.length === 0 && <tr>
                        <td colSpan={10} className="text-center">
                            Brak zadań do wyświetlenia.
                        </td>
                    </tr>}

                    {tasks.map(task => (
                        <tr key={task.id ? task.id : '-'}>
                            <td>{task.name || '—'}</td>
                            <td>{task.description || '—'}</td>
                            <td>
                                {(() => {
                                    const project = projects.find(p => p.id === task.projectId);
                                    return project ? project.name : '—';
                                })()}
                            </td>
                            <td>
                                {(() => {
                                    const student = listStudents.find(s => String(s.id) === String(task.assignedStudentId));
                                    return student ? `${student.name} ${student.surname}` : '—';
                                })()}
                            </td>
                            <td>
                                {(() => {
                                    const teacher = listTeachers.find(t => t.id === task.teacherId);
                                    return teacher ? `${teacher.name} ${teacher.surname}` : '—';
                                })()}
                            </td>
                            <td>{task.fileIds.join(', ') || '—'}</td>
                            <td>{task.priority || '—'}</td>
                            <td>{task.creationDate ? new Date(task.creationDate).toISOString().slice(0, 10)
                                : '—'}</td>
                            <td>{task.doneDate ? new Date(task.doneDate).toISOString().slice(0, 10)
                                : '—'}</td>
                            <td>
                                {userRole === "TEACHER" && <Button size="sm" onClick={() => handleEdit(task)} className="me-2">Edytuj</Button>}
                                {userRole === "TEACHER" && <Button size="sm" variant="danger" onClick={() => handleDelete(task.id)}>Usuń</Button>}
                            </td>
                        </tr>
                    ))}
                </tbody>
            </Table>

            {/* Modal do edycji/dodania */}
            <Modal show={showModal} onHide={() => setShowModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>{editingTask ? 'Edytuj Zadanie' : 'Nowe Zadanie'}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>

                        <Form.Group className="mb-2">
                            <Form.Label>Nazwa zadania</Form.Label>
                            <Form.Control
                                type="text"
                                value={form.name}
                                onChange={(e) => setForm({ ...form, name: e.target.value })}
                                minLength={2}
                                maxLength={50}
                                required
                            />
                        </Form.Group>

                        <Form.Group className="mb-2">
                            <Form.Label>Opis zadania</Form.Label>
                            <Form.Control
                                as="textarea"
                                rows={1}
                                value={form.description}
                                onChange={(e) => setForm({ ...form, description: e.target.value })}
                                minLength={10}
                                maxLength={200}
                            />
                        </Form.Group>

                        <Form.Group className="mb-2">
                            <Form.Label>Nazwa projektu</Form.Label>
                            <Form.Select
                                value={form.projectId}
                                onChange={(e) => setForm({ ...form, projectId: e.target.value })}
                            >
                                <option value="">Wybierz Projekt</option>
                                {projects.map((project) => (
                                    <option key={project.id} value={project.id}>
                                        {project.name}
                                    </option>
                                ))}
                            </Form.Select>
                        </Form.Group>

                        <Form.Group className="mb-2">
                            <Form.Label>Nazwa studenta</Form.Label>
                            <Form.Select
                                value={form.assignedStudentId}
                                onChange={(e) => setForm({ ...form, assignedStudentId: e.target.value })}
                            >
                                {listStudents.map((student) => (
                                    <option key={student.id} value={student.id}>
                                        {student.name} {student.surname}
                                    </option>
                                ))}
                            </Form.Select>
                        </Form.Group>

                        <Form.Group className="mb-2">
                            <Form.Label>Nazwa nauczyciela</Form.Label>
                            <Form.Select
                                value={form.teacherId}
                                onChange={(e) => setForm({ ...form, teacherId: e.target.value })}
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
                            <Form.Label>Priorytet</Form.Label>
                            <Form.Control
                                type="number"
                                value={form.priority}
                                onChange={(e) => setForm({ ...form, priority: e.target.value })}
                                min={1}
                            />
                        </Form.Group>

                        <Form.Group>
                            <Form.Label>creationDate</Form.Label>
                            <Form.Control
                                type="date"
                                value={form.creationDate}
                                onChange={(e) => setForm({ ...form, creationDate: e.target.value })}
                            />
                        </Form.Group>

                        <Form.Group className="mb-2">
                            <Form.Label>doneDate</Form.Label>
                            <Form.Control
                                type="date"
                                value={form.doneDate}
                                onChange={(e) => setForm({ ...form, doneDate: e.target.value })}
                            />
                        </Form.Group>

                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowModal(false)}>Anuluj</Button>
                    <Button variant="success" onClick={handleSave}>Zapisz</Button>
                </Modal.Footer>
            </Modal>
        </Container>
    );
}

export default TaskView;