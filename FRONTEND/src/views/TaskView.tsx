import {UserRole} from "../models/auth/UserRole.ts";
import { useState, useEffect } from 'react';
import { getAllTasks, createTask, modifyTask, deleteTask } from '../controllers/taskController';
import { Table, Button, Modal, Form, Container } from 'react-bootstrap';
import { Task } from '../models/Task';

function TaskView() {
    const [tasks, setTasks] = useState<Task[]>([]);
    const [showModal, setShowModal] = useState(false);
    const [editingTask, setEditingTask] = useState<Task | null>(null);

    const userRole: UserRole = sessionStorage.getItem("accessRole") as UserRole;

    const emptyTask: Task = {
        id: '',
        name: '',
        description: '',
        priority: 0,
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
    }, []);

    const handleGetTasks = async () => {
        try {
            const response = await getAllTasks();
            setTasks(response);
        } catch (error) {
            console.error("Error fetching tasks:", error);
        }
    };

    const handleDelete = async (id: string) => {
        try {
            await deleteTask(id);
            console.log("Task deleted:", id);
            handleGetTasks();
        } catch (error) {
            console.error("Error deleting task:", error);
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
            console.error("Error saving task:", error);
        }
    };

    return (
        <Container className="mt-4">
            <h2>Zarządzanie Zadaniami</h2>
            {userRole === "TEACHER" && <Button variant="primary" onClick={handleCreate} className="mb-3">Dodaj Zadanie</Button>}
            <Table responsive>
                <thead>
                    <tr>
                        <th>Nazwa</th>
                        <th>Opis</th>
                        <th>Priorytet</th>
                        <th>Student</th>
                        <th>Projekt</th>
                        <th>Nauczyciel</th>
                        <th>Pliki</th>
                        <th>Data Utworzenia</th>
                        <th>Data Zakończenia</th>
                        <th>Akcje</th>
                    </tr>
                </thead>
                <tbody>
                    {tasks.map(task => (
                        <tr key={task.id ? task.id : '-'}>
                            <td>{task.name || '—'}</td>
                            <td>{task.description || '—'}</td>
                            <td>{task.priority || '—'}</td>
                            <td>{task.assignedStudentId || '—'}</td>
                            <td>{task.projectId || '—'}</td>
                            <td>{task.teacherId || '—'}</td>
                            <td>{task.fileIds.join(', ') || '—'}</td>
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
                        {['name', 'description', 'projectId', 'teacherId', 'fileIds'].map(field => (
                            <Form.Group key={field} className="mb-2">
                                <Form.Label>{field}</Form.Label>
                                <Form.Control
                                    type="text"
                                    value={form[field]}
                                    onChange={(e) => setForm({ ...form, [field]: e.target.value })}
                                />
                            </Form.Group>
                        ))}
                        <Form.Group className="mb-2">
                            <Form.Label>assignedStudentId</Form.Label>
                            <Form.Control
                                type="number"
                                value={form.assignedStudentId}
                                onChange={(e) => setForm({ ...form, assignedStudentId: e.target.value })}
                            />
                        </Form.Group>
                        <Form.Group className="mb-2">
                            <Form.Label>priority</Form.Label>
                            <Form.Control
                                type="number"
                                value={form.priority}
                                onChange={(e) => setForm({ ...form, priority: e.target.value })}
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