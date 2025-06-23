import {UserRole} from "../models/auth/UserRole.ts";
import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Container, Table, Button, Form, Modal } from 'react-bootstrap';
import { Project } from '../models/Project';
import { Task } from '../models/Task';
import { getProjectById } from '../controllers/projectController';
import { getTaskById, modifyTask, createTask, deleteTask } from '../controllers/taskController';
import {toast} from "react-toastify";

function ProjectByIdView() {
    const { id } = useParams();
    const [project, setProject] = useState<Project | null>(null);
    const [tasks, setTasks] = useState<Task[]>([]);
    const [editingTask, setEditingTask] = useState<Task | null>(null);
    const [showModal, setShowModal] = useState(false);

    const userRole: UserRole = localStorage.getItem("accessRole") as UserRole;

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
        if (id) {
            handleGetProject(id);
        }
    }, [id]);

    const handleGetProject = async (id: string) => {
        try {
            const fetchedProject = await getProjectById(id);
            setProject(fetchedProject);

            if (fetchedProject.taskIds.length > 0) {
                const tasksRes = await Promise.all(
                    fetchedProject.taskIds.map((taskId: string) => getTaskById(taskId))
                );
                setTasks(tasksRes);
            }
        } catch (error) {
            toast('Błąd podczas pobierania projektu: ' + error);
        }
    };

    const handleDelete = async (id: string) => {
        try {
            await deleteTask(id);
            console.log("Task deleted:", id);
            handleGetProject(project?.id || '');
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
            projectId: project?.id || '',
            assignedStudentId: parseInt(form.assignedStudentId),
            priority: parseInt(form.priority),
            doneDate: new Date(form.doneDate),
            creationDate: new Date(form.creationDate),
        };

        try {
            if (editingTask) {
                await modifyTask(payload);
            } else {
                await createTask(payload);
            }
            setShowModal(false);
            handleGetProject(project?.id || '');
        } catch (error) {
            toast("Error saving task: " + error);
        }
    };

    return (
        <Container className="mt-4">
            <h2>Szczegóły Projektu: {project?.name}</h2>
            <p><strong>Opis:</strong> {project?.description}</p>
            <p><strong>Nauczyciel:</strong> {project?.teacherId}</p>
            <p><strong>Studenci:</strong> {project?.studentIds?.join(', ')}</p>
            <p><strong>Pliki:</strong> {project?.fileIds?.join(', ')}</p>
            <p><strong>Data utworzenia:</strong> {project?.creationDate ? new Date(project.creationDate).toLocaleDateString() : ''}</p>
            <p><strong>Data zakończenia:</strong> {project?.doneDate ? new Date(project.doneDate).toLocaleDateString() : ''}</p>

            {userRole === "TEACHER" && <Button variant="primary" onClick={handleCreate} className="mb-3">Dodaj Zadanie</Button>}
            <h4 className="mt-4">Zadania</h4>
            {tasks.length > 0 ? (
                <Table responsive>
                    <thead>
                        <tr>
                            <th>Nazwa</th>
                            <th>Opis</th>
                            <th>Priorytet</th>
                            <th>Przypisany student</th>
                            <th>Nauczyciel</th>
                            <th>Pliki</th>
                            <th>Data utworzenia</th>
                            <th>Data zakończenia</th>
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
                            <tr key={task.id}>
                                <td>{task.name}</td>
                                <td>{task.description}</td>
                                <td>{task.priority}</td>
                                <td>{task.assignedStudentId}</td>
                                <td>{task.teacherId}</td>
                                <td>{task.fileIds.join(', ')}</td>
                                <td>{new Date(task.creationDate).toLocaleDateString()}</td>
                                <td>{new Date(task.doneDate).toLocaleDateString()}</td>
                                <td>
                                    {userRole === "TEACHER" && <Button size="sm" onClick={() => handleEdit(task)} className="me-2">Edytuj</Button>}
                                    {userRole === "TEACHER" && <Button size="sm" variant="danger" onClick={() => handleDelete(task.id)}>Usuń</Button>}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            ) : (
                <p>Brak przypisanych zadań.</p>
            )}

            {/* Modal do edycji/dodania */}
            <Modal show={showModal} onHide={() => setShowModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>{editingTask ? 'Edytuj Zadanie' : 'Nowe Zadanie'}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        {['name', 'description', 'teacherId', 'fileIds'].map(field => (
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

export default ProjectByIdView;
