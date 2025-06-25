import React, { useState, ChangeEvent } from 'react';
import { Modal, Button, Form, Spinner, Alert } from 'react-bootstrap';
import {toast} from "react-toastify";

interface FileUploadModalProps {
    show: boolean;
    onHide: () => void;
    projectId: string;
    taskId?: string;
    mode: "project" | "task";
}

const FileUploadModal: React.FC<FileUploadModalProps> = ({ show, onHide, projectId, mode, taskId }) => {
    const [selectedFile, setSelectedFile] = useState<File | null>(null);
    const [isUploading, setIsUploading] = useState<boolean>(false);
    const [error, setError] = useState<string>('');
    const [success, setSuccess] = useState<boolean>(false);

    const handleFileChange = (event: ChangeEvent<HTMLInputElement>) => {
        const file = event.target.files?.[0] || null;
        setSelectedFile(file);
        setSuccess(false);
        setError('');
    };

    const handleUpload = async () => {
        if (!selectedFile) {
            setError('Nie wybrano pliku.');
            return;
        }

        const formData = new FormData();
        formData.append('file', selectedFile);

        setIsUploading(true);
        setError('');
        setSuccess(false);

        try {
            const endpoint = mode === "task" ? `task/${taskId}/file` : `project/${projectId}/file`;

            const response = await fetch(`http://localhost:8080/api/v1/${endpoint}`, {
                method: 'POST',
                body: formData,
                headers: {
                    Authorization: `Bearer ${localStorage.getItem('accessToken') || ''}`,
                    ContentType: 'multipart/form-data',
                }
            });

            if (!response.ok) {
                throw new Error(`Błąd serwera: ${response.status}`);
            }

            toast("Plik został przesłany pomyślnie!");
            setSuccess(true);
            setSelectedFile(null);
            onHide();
        } catch (err: any) {
            setError(err.message || 'Wystąpił błąd podczas przesyłania.');
        } finally {
            setIsUploading(false);
        }
    };

    const handleClose = () => {
        setSelectedFile(null);
        setError('');
        setSuccess(false);
        onHide();
    };

    return (
        <Modal show={show} onHide={handleClose} centered>
            <Modal.Header closeButton>
                <Modal.Title>Prześlij plik do {mode === "task" ? "zadania" : "projektu"}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form>
                    <Form.Group controlId="formFile">
                        <Form.Label>Wybierz plik</Form.Label>
                        <Form.Control type="file" onChange={handleFileChange} />
                    </Form.Group>
                </Form>
                {error && <Alert variant="danger" className="mt-3">{error}</Alert>}
                {success && <Alert variant="success" className="mt-3">Plik został przesłany!</Alert>}
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                    Anuluj
                </Button>
                <Button
                    variant="primary"
                    onClick={handleUpload}
                    disabled={isUploading}
                >
                    {isUploading ? (
                        <>
                            <Spinner animation="border" size="sm" className="me-2" />
                            Wysyłanie...
                        </>
                    ) : (
                        'Prześlij'
                    )}
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

export default FileUploadModal;
