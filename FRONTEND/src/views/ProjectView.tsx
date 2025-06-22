import { useState, useEffect } from 'react';
import { getAllProjects, createProject, modifyProject, deleteProject } from '../controllers/projectController';
import { Table, Button, Modal, Form } from 'react-bootstrap';
import { Project } from '../models/Project';
import { useNavigate } from 'react-router-dom';
import {toast} from "react-toastify";

function ProjectView() {
    const [projects, setProjects] = useState<Project[]>([]);
    const [showModal, setShowModal] = useState(false);
    const [editingProject, setEditingProject] = useState<Project | null>(null);

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
    }, []);

    const handleGetProjects = async () => {
        try {
            const response = await getAllProjects();
            setProjects(response);
        } catch (error) {
            toast("Error fetching projects: " + error);
        }
    };

    const handleDelete = async (id: string) => {
        try {
            await deleteProject(id);
            console.log("Project deleted:", id);
            handleGetProjects();
        } catch (error) {
            toast("Error deleting project: " + error);
        }
    };

    const handleEdit = (project: Project) => {
        setEditingProject(project);
        setForm({
            ...project,
            fileIds: project.fileIds.join(', '),
            studentIds: project.studentIds.join(', '),
            taskIds: project.taskIds.join(', '),
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
            fileIds: (form.fileIds ?? []).split(','),
            studentIds: (form.studentIds ?? []).split(','),
            taskIds: (form.taskIds ?? []).split(','),
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
            toast("Error saving project:" + error);
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
            <Button variant="primary" onClick={handleCreate} className="mb-3">Dodaj Projekt</Button>
            <Table responsive striped bordered hover>
                <thead>
                    <tr>
                        <th>Nazwa</th>
                        <th>Opis</th>
                        <th>ID Nauczyciela</th>
                        <th>ID Studentów</th>
                        <th>ID Zadań</th>
                        <th>ID Plików</th>
                        <th>Data Utworzenia</th>
                        <th>Data Zakończenia</th>
                        <th>Akcje</th>
                    </tr>
                </thead>
                <tbody>
                    {projects.map((project) => (
                        <tr key={project.id}>
                            <td>{project.name}</td>
                            <td>{project.description}</td>
                            <td>{project.teacherId}</td>
                            <td>{project.studentIds.join(', ')}</td>
                            <td>{project.taskIds.join(', ')}</td>
                            <td>{project.fileIds.join(', ')}</td>
                            <td>{project.creationDate ? new Date(project.creationDate).toISOString().slice(0, 10) : ''}</td>
                            <td>{project.doneDate ? new Date(project.doneDate).toISOString().slice(0, 10) : ''}</td>
                            <td>
                                <Button size="sm" onClick={() => handleEdit(project)} className="me-2">Edytuj</Button>
                                <Button size="sm" variant="warning" onClick={() => navigate(`/project/${project.id}`)} className="me-2">Zobacz</Button>
                                <Button size="sm" variant="danger" onClick={() => handleDelete(project.id)}>Usuń</Button>
                            </td>
                        </tr>
                    ))}
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
                            <Form.Label>ID Nauczyciela</Form.Label>
                            <Form.Control
                                type="text"
                                value={form.teacherId}
                                onChange={(e) => handleInputChange('teacherId', e.target.value)}
                            />
                        </Form.Group>
                        <Form.Group className="mb-2">
                            <Form.Label>ID Studentów (oddzielone przecinkami)</Form.Label>
                            <Form.Control
                                type="text"
                                value={form.studentIds}
                                onChange={(e) => handleInputChange('studentIds', e.target.value)}
                            />
                        </Form.Group>
                        <Form.Group className="mb-2">
                            <Form.Label>ID Zadań (oddzielone przecinkami)</Form.Label>
                            <Form.Control
                                type="text"
                                value={form.taskIds}
                                onChange={(e) => handleInputChange('taskIds', e.target.value)}
                            />
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
