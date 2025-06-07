import { useState, useEffect } from 'react';
import { getAllProjects, getProjectById, createProject, modifyProject, deleteProject } from '../controllers/projectController';
import { Table, Button, Modal, Form } from 'react-bootstrap';
import { Project } from '../models/Project';

function ProjectView() {
    const [projects, setProjects] = useState<Project[]>([]);
    const [showModal, setShowModal] = useState(false);
    const [editingProject, setEditingProject] = useState<Project | null>(null);

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
            setProjects(response.data);
        } catch (error) {
            console.error("Error fetching projects:", error);
        }
    };

    const handleDelete = async (id: string) => {
        try {
            await deleteProject(id);
            console.log("Project deleted:", id);
            handleGetProjects();
        } catch (error) {
            console.error("Error deleting project:", error);
        }
    };

    const handleEdit = (project: Project) => {
        setEditingProject(project);
        setForm({
            ...project,
            fileIds: project.fileIds.join(', '),
            studentIds: project.studentIds.join(', '),
            taskIds: project.taskIds.join(', '),
            creationDate: new Date(project.creationDate).toISOString().slice(0, 10),
            doneDate: new Date(project.doneDate).toISOString().slice(0, 10),
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
            fileIds: form.fileIds.split(',').map((id) => id.trim()),
            studentIds: form.studentIds.split(',').map((id) => id.trim()),
            taskIds: form.taskIds.split(',').map((id) => id.trim()),
            creationDate: new Date(form.creationDate),
            doneDate: new Date(form.doneDate),
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
            console.error("Error saving project:", error);
        }
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
                            <td>{new Date(project.creationDate).toISOString().slice(0, 10)}</td>
                            <td>{new Date(project.doneDate).toISOString().slice(0, 10)}</td>
                            <td>
                                <Button size="sm" onClick={() => handleEdit(project)} className="me-2">Edytuj</Button>
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
                        {['name', 'description', 'teacherId', 'studentIds', 'fileIds', 'taskIds'].map(field => (
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
                            <Form.Label>Data Utworzenia</Form.Label>
                            <Form.Control
                                type="date"
                                value={form.creationDate}
                                onChange={(e) => setForm({ ...form, creationDate: e.target.value })}
                            />
                        </Form.Group>
                        <Form.Group className="mb-2">
                            <Form.Label>Data Zakończenia</Form.Label>
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
        </div>
    );
}

export default ProjectView;
