import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Container, Table, Button, Form, Modal } from 'react-bootstrap';
import { toast } from "react-toastify";

import { UserRole } from "../models/auth/UserRole.ts";
import { Project } from '../models/Project.ts';
import { Teacher } from "../models/Teacher.ts";
import { Student } from "../models/Student.ts";
import { Task } from "../models/Task.ts";

import { getProjectById } from '../controllers/projectController';
import { getTaskById, modifyTask, createTask, deleteTask } from '../controllers/taskController';
import { getAllStudents, getAllTeachers } from '../controllers/listUsersController.ts';

function ProjectByIdView() {
    const { id } = useParams<{ id: string }>();
    const [project, setProject] = useState<Project | null>(null);
    const [tasks, setTasks] = useState<Task[]>([]);
    const [editingTask, setEditingTask] = useState<Task | null>(null);
    const [showModal, setShowModal] = useState(false);
    const [teachers, setTeachers] = useState<Teacher[]>([]);
    const [students, setStudents] = useState<Student[]>([]);

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

    const [form, setForm] = useState({
        ...emptyTask,
        fileIds: '',
        creationDate: '',
        doneDate: ''
    });

    useEffect(() => {
        if (!id) return;
        loadProjectAndTasks(id);
        loadTeachers();
        loadStudents();
    }, [id]);

    const loadProjectAndTasks = async (projectId: string) => {
        try {
            const fetchedProject = await getProjectById(projectId);
            setProject(fetchedProject);

            if (fetchedProject.taskIds?.length > 0) {
                const fetchedTasks = await Promise.all(fetchedProject.taskIds.map(getTaskById));
                setTasks(fetchedTasks);
            } else {
                setTasks([]);
            }
        } catch (error) {
            toast.error(`Błąd podczas pobierania projektu: ${error}`);
        }
    };

    const loadTeachers = async () => {
        try {
            const result = await getAllTeachers();
            setTeachers(result);
        } catch (error) {
            toast.error(`Błąd pobierania nauczycieli: ${error}`);
        }
    };

    const loadStudents = async () => {
        try {
            const result = await getAllStudents();
            setStudents(result);
        } catch (error) {
            toast.error(`Błąd pobierania studentów: ${error}`);
        }
    };

    const handleDeleteTask = async (taskId: string) => {
        try {
            await deleteTask(taskId);
            if (project?.id) loadProjectAndTasks(project.id);
        } catch (error) {
            toast.error(`Błąd usuwania zadania: ${error}`);
        }
    };

    const handleEditTask = (task: Task) => {
        setEditingTask(task);
        setForm({
            ...task,
            fileIds: task.fileIds.join(','),
            creationDate: task.creationDate ? new Date(task.creationDate).toISOString().slice(0, 10) : '',
            doneDate: task.doneDate ? new Date(task.doneDate).toISOString().slice(0, 10) : ''
        });
        setShowModal(true);
    };

    const handleCreateTask = () => {
        setEditingTask(null);
        setForm({
            ...emptyTask,
            fileIds: '',
            creationDate: '',
            doneDate: ''
        });
        setShowModal(true);
    };

    const handleSaveTask = async () => {
        const payload: Task = {
            ...form,
            fileIds: form.fileIds.split(',').map(id => id.trim()),
            projectId: project?.id || '',
            assignedStudentId: Number(form.assignedStudentId),
            priority: Number(form.priority),
            creationDate: new Date(form.creationDate),
            doneDate: new Date(form.doneDate)
        };

        try {
            if (editingTask) {
                await modifyTask(payload);
            } else {
                await createTask(payload);
            }
            setShowModal(false);
            if (project?.id) loadProjectAndTasks(project.id);
        } catch (error) {
            toast.error(`Błąd zapisu zadania: ${error}`);
        }
    };

    const projectTeacher = teachers.find(t => t.id === project?.teacherId);
    const projectStudents = students.filter(s => project?.studentIds.includes(String(s.id)));

    return (
        <Container className="mt-4">
            <h2>Projekt: {project?.name}</h2>
            <p><strong>Opis:</strong> {project?.description}</p>
            <p><strong>Nauczyciel:</strong> {projectTeacher ? `${projectTeacher.name} ${projectTeacher.surname}` : 'Brak'}</p>
            <p><strong>Studenci:</strong> {projectStudents.map(s => `${s.name} ${s.surname}`).join(', ') || 'Brak'}</p>
            <p><strong>Pliki:</strong> {project?.fileIds?.join(', ')}</p>
            <p><strong>Data utworzenia:</strong> {project?.creationDate && new Date(project.creationDate).toLocaleDateString()}</p>
            <p><strong>Data zakończenia:</strong> {project?.doneDate && new Date(project.doneDate).toLocaleDateString()}</p>

            {userRole === "TEACHER" && (
                <Button className="mb-3" onClick={handleCreateTask}>Dodaj Zadanie</Button>
            )}

            <h4>Zadania</h4>
            {tasks.length > 0 ? (
                <Table responsive striped bordered hover>
                    <thead>
                        <tr>
                            <th>Nazwa</th>
                            <th>Opis</th>
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
                        {tasks.map(task => {
                            const student = students.find(s => String(s.id) === String(task.assignedStudentId));
                            const teacher = teachers.find(t => t.id === task.teacherId);
                            return (
                                <tr key={task.id}>
                                    <td>{task.name}</td>
                                    <td>{task.description}</td>
                                    <td>{student ? `${student.name} ${student.surname}` : 'Brak'}</td>
                                    <td>{teacher ? `${teacher.name} ${teacher.surname}` : 'Brak'}</td>
                                    <td>{task.fileIds.join(', ')}</td>
                                    <td>{task.priority}</td>
                                    <td>{new Date(task.creationDate).toLocaleDateString()}</td>
                                    <td>{new Date(task.doneDate).toLocaleDateString()}</td>
                                    <td>
                                        {userRole === "TEACHER" && (
                                            <>
                                                <Button size="sm" onClick={() => handleEditTask(task)} className="me-2">Edytuj</Button>
                                                <Button size="sm" variant="danger" onClick={() => handleDeleteTask(task.id)}>Usuń</Button>
                                            </>
                                        )}
                                    </td>
                                </tr>
                            );
                        })}
                    </tbody>
                </Table>
            ) : (
                <p>Brak przypisanych zadań.</p>
            )}

            {/* Modal formularza */}
            <Modal show={showModal} onHide={() => setShowModal(false)}>
                <Modal.Header closeButton>
                    <Modal.Title>{editingTask ? 'Edytuj Zadanie' : 'Nowe Zadanie'}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>

                        <Form.Group className="mb-2">
                            <Form.Label>Nazwa</Form.Label>
                            <Form.Control
                                type="text"
                                value={form.name}
                                onChange={e => setForm({ ...form, name: e.target.value })}
                            />
                        </Form.Group>

                        <Form.Group className="mb-2">
                            <Form.Label>Opis</Form.Label>
                            <Form.Control
                                as="textarea"
                                rows={1}
                                value={form.description}
                                onChange={e => setForm({ ...form, description: e.target.value })}
                            />
                        </Form.Group>

                        <Form.Group className="mb-2">
                            <Form.Label>Student</Form.Label>
                            <Form.Select
                                value={form.assignedStudentId}
                                onChange={e => setForm({ ...form, assignedStudentId: Number(e.target.value) })}
                            >
                                <option value="">Wybierz studenta</option>
                                {students.map(s => (
                                    <option key={s.id} value={s.id}>
                                        {s.name} {s.surname}
                                    </option>
                                ))}
                            </Form.Select>
                        </Form.Group>

                        <Form.Group className="mb-2">
                            <Form.Label>Nauczyciel</Form.Label>
                            <Form.Select
                                value={form.teacherId}
                                onChange={e => setForm({ ...form, teacherId: e.target.value })}
                            >
                                <option value="">Wybierz nauczyciela</option>
                                {teachers.map(t => (
                                    <option key={t.id} value={t.id}>
                                        {t.name} {t.surname}
                                    </option>
                                ))}
                            </Form.Select>
                        </Form.Group>

                        <Form.Group className="mb-2">
                            <Form.Label>ID Plików</Form.Label>
                            <Form.Control
                                type="text"
                                value={form.fileIds}
                                onChange={e => setForm({ ...form, fileIds: e.target.value })}
                                placeholder="np. file1,file2"
                            />
                        </Form.Group>

                        <Form.Group className="mb-2">
                            <Form.Label>Priorytet</Form.Label>
                            <Form.Control
                                type="number"
                                value={form.priority}
                                onChange={e => setForm({ ...form, priority: Number(e.target.value) })}
                            />
                        </Form.Group>

                        <Form.Group className="mb-2">
                            <Form.Label>Data utworzenia</Form.Label>
                            <Form.Control
                                type="date"
                                value={form.creationDate}
                                onChange={e => setForm({ ...form, creationDate: e.target.value })}
                            />
                        </Form.Group>

                        <Form.Group className="mb-2">
                            <Form.Label>Data zakończenia</Form.Label>
                            <Form.Control
                                type="date"
                                value={form.doneDate}
                                onChange={e => setForm({ ...form, doneDate: e.target.value })}
                            />
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => setShowModal(false)}>Anuluj</Button>
                    <Button variant="success" onClick={handleSaveTask}>Zapisz</Button>
                </Modal.Footer>
            </Modal>
        </Container>
    );
}

export default ProjectByIdView;
