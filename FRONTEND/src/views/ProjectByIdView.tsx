import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import {Container, Table, Button, Form, Modal, Badge, CloseButton} from 'react-bootstrap';
import { toast } from "react-toastify";

import { UserRole } from "../models/auth/UserRole.ts";
import { Project } from '../models/Project.ts';
import { Teacher } from "../models/Teacher.ts";
import { Student } from "../models/Student.ts";
import { Task } from "../models/Task.ts";

import { getProjectById } from '../controllers/projectController';
import { getTaskById, modifyTask, createTask, deleteTask } from '../controllers/taskController';
import { getAllStudents, getAllTeachers } from '../controllers/listUsersController.ts';
import FileUploadModal from "../components/FileUploadModal.tsx";

function ProjectByIdView() {
    const { id } = useParams<{ id: string }>();
    const [project, setProject] = useState<Project | null>(null);
    const [tasks, setTasks] = useState<Task[]>([]);
    const [editingTask, setEditingTask] = useState<Task | null>(null);
    const [showModal, setShowModal] = useState(false);
    const [teachers, setTeachers] = useState<Teacher[]>([]);
    const [students, setStudents] = useState<Student[]>([]);

    const userRole: UserRole = localStorage.getItem("accessRole") as UserRole;

    const [taskToUploadFile, setTaskToUploadFile] = useState<Task | null>(null);

    const emptyTask: Task = {
        id: '',
        name: '',
        description: '',
        priority: 1,
        assignedStudentId: null,
        projectId: '',
        teacherId: '',
        fileIds: [],
        creationDate: new Date(),
        doneDate: null
    };

    const [form, setForm] = useState({
        ...emptyTask,
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

            const whoami = await fetch("http://localhost:8080/api/v1/whoami", {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('accessToken')}`
                }
            });
            const whoamiData = await whoami.text();

            if (fetchedProject.taskIds?.length > 0) {
                let fetchedTasks = await Promise.all(fetchedProject.taskIds.map(getTaskById));
                if (localStorage.getItem("accessRole") === "STUDENT") {
                    fetchedTasks = fetchedTasks.filter(task => task.assignedStudentId === whoamiData);
                }

                setTasks(fetchedTasks);
            } else {
                setTasks([]);
            }
        } catch (error) {
            console.error(`Błąd podczas pobierania projektu: ${error}`);
        }
    };

    const loadTeachers = async () => {
        try {
            const result = await getAllTeachers();
            setTeachers(result);
        } catch (error) {
            console.error(`Błąd pobierania nauczycieli: ${error}`);
        }
    };

    const loadStudents = async () => {
        try {
            const result = await getAllStudents();
            setStudents(result);
        } catch (error) {
            console.error(`Błąd pobierania studentów: ${error}`);
        }
    };

    const handleDeleteTask = async (taskId: string) => {
        try {
            await deleteTask(taskId);
            loadProjectAndTasks(id!);
        } catch (error) {
            loadProjectAndTasks(id!);
        }
    };

    const handleEditTask = (task: Task) => {
        setEditingTask(task);
        setForm({
            ...task,
            creationDate: task.creationDate ?? new Date(),
            doneDate: task.doneDate ?? null
        });
        setShowModal(true);
    };

    const handleCreateTask = () => {
        setEditingTask(null);
        setForm({
            ...emptyTask,
        });
        setShowModal(true);
    };

    const handleSaveTask = async () => {
        const payload: Task = {
            ...form,
            projectId: project?.id || '',
            assignedStudentId: form.assignedStudentId,
            priority: Number(form.priority),
            creationDate: form.creationDate ? new Date(form.creationDate) : null,
            doneDate: form.doneDate ? new Date(form.doneDate) : null
        };

        try {
            if (editingTask) {
                await modifyTask(payload);
            } else {
                await createTask(payload);
            }
            setShowModal(false);
            loadProjectAndTasks(id!);
        } catch (error) {
            toast.error(`Błąd zapisu zadania: ${error}`);
        }
    };

    const projectTeacher = teachers.find(t => t.id === project?.teacherId);
    const projectStudents = students.filter(s => project?.studentIds.includes(String(s.id)));

    function handleDeleteProjectFile(event: React.MouseEvent<HTMLButtonElement>, id: string, fileId: string) {
        event.stopPropagation();
        event.preventDefault();

        fetch(`http://localhost:8080/api/v1/project/${id}/file/${fileId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`Błąd usuwania pliku: ${response.status}`);
                }
                toast.success("Plik został usunięty pomyślnie!");
                loadProjectAndTasks(id);
            })
            .catch(error => {
                toast.error(`Błąd podczas usuwania pliku: ${error}`);
            });
    }

    return (
        <Container className="mt-4">
            <h2>Projekt: {project?.name}</h2>
            <p><strong>Opis:</strong> {project?.description}</p>
            <p><strong>Nauczyciel:</strong> {projectTeacher ? `${projectTeacher.name} ${projectTeacher.surname}` : 'Brak'}</p>
            <p><strong>Studenci:</strong> {projectStudents.map(s => `${s.name} ${s.surname}`).join(', ') || 'Brak'}</p>
            <p>
                <strong>Pliki: </strong><br />
                {(project?.fileIds ?? []).map(fileId =>
                    <a href={`http://localhost:8080/api/v1/project/${project!.id}/file/${fileId}`} key={fileId} target={"_blank"}>
                        <Badge className={"d-inline-flex align-items-center me-2 m-1"}>
                            Załącznik: {fileId.split("_")[2]}

                            {userRole === "TEACHER" && <CloseButton
                                onClick={(event) => handleDeleteProjectFile(event, project!.id, fileId)}
                                className="ms-2"
                                variant="white"
                                aria-label="Usuń"
                            />}
                        </Badge>
                    </a>
                )}
                {project?.fileIds && project.fileIds.length === 0 && <span>-</span>}

            </p>
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
                                    <td>
                                        {task.fileIds.map((fileId) => (
                                            <a href={`http://localhost:8080/api/v1/task/${task.id}/file/${fileId}`} key={fileId} target="_blank">
                                                <Badge>
                                                    Załącznik: {fileId.split("_")[2]}
                                                </Badge>
                                            </a>
                                        ))}
                                    </td>

                                    <td>{task.priority}</td>
                                    <td>{new Date(task.creationDate ?? '').toLocaleDateString()}</td>
                                    <td>{task.doneDate ? new Date(task.doneDate).toLocaleDateString() : "-"}</td>

                                    <td>
                                        {userRole === "TEACHER" && (
                                            <>
                                                <Button size="sm" onClick={() => handleEditTask(task)} className="me-2">Edytuj</Button>
                                                <Button size="sm" variant="danger" onClick={() => handleDeleteTask(task.id)}>Usuń</Button>
                                            </>
                                        )}
                                        {userRole === "STUDENT" && (
                                            <Button size="sm"
                                                    onClick={() => setTaskToUploadFile(task)}
                                                    className="me-2">
                                                {task.fileIds.length > 0 ? "Podmień efekt pracy" : "Dodaj efekt pracy"}
                                            </Button>
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
                    <Form onSubmit={event => {event.preventDefault(); handleSaveTask();}}>

                        <Form.Group className="mb-2">
                            <Form.Label>Nazwa</Form.Label>
                            <Form.Control
                                type="text"
                                required
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
                                value={form.assignedStudentId?.toString()}
                                onChange={e => setForm({ ...form, assignedStudentId: e.target.value })}>
                                <option value="">Wybierz studenta</option>
                                {students.map(s => (
                                    <option key={s.id} value={s.id}>
                                        {s.name} {s.surname}
                                    </option>
                                ))}
                            </Form.Select>
                        </Form.Group>

                        <Form.Group className="mb-2">
                            <Form.Label>Priorytet</Form.Label>
                            <Form.Control
                                type="number"
                                value={form.priority}
                                min={1}
                                required
                                onChange={e => setForm({ ...form, priority: Number(e.target.value) })}
                            />
                        </Form.Group>

                        <Modal.Footer>
                            <Button variant="secondary" onClick={() => setShowModal(false)}>Anuluj</Button>
                            <Button variant="success" type={"submit"}>Zapisz</Button>
                        </Modal.Footer>
                    </Form>
                </Modal.Body>
            </Modal>

            {taskToUploadFile && (
                <FileUploadModal
                    show={!!taskToUploadFile}
                    onHide={() => loadProjectAndTasks(id!).then(() =>setTaskToUploadFile(null))}
                    projectId={id!}
                    taskId={taskToUploadFile.id}
                    mode={"task"}
                />
            )}
        </Container>
    );
}

export default ProjectByIdView;
