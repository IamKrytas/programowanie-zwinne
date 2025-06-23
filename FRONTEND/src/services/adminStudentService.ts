import { Student } from "../models/Student";

export const getAllStudentsService = async (): Promise<Student[]> => {
    const token = localStorage.getItem("accessToken");
    const API_URL = "http://localhost:8080";
    const response = await fetch(`${API_URL}/api/v1/admin/student?offset=0&limit=10`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
    });
    if (!response.ok) {
        throw new Error(`Błąd: ${response.status}`);
    }
    const data = await response.json();
    return data;
}

export const getStudentByIdService = async (id: string): Promise<Student> => {
    const token = localStorage.getItem("accessToken");
    const API_URL = "http://localhost:8080";
    const response = await fetch(`${API_URL}/api/v1/admin/student/${id}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
    });
    if (!response.ok) {
        throw new Error(`Błąd: ${response.status}`);
    }
    const data = await response.json();
    return data;
}

export const createStudentService = async (student: Student): Promise<Student> => {
    const token = localStorage.getItem("accessToken");
    const API_URL = "http://localhost:8080";
    const response = await fetch(`${API_URL}/api/v1/admin/student`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(student),
    });
    if (!response.ok) {
        throw new Error(`Błąd: ${response.status}`);
    }
    const data = await response.json();
    return data;
}

export const modifyStudentByIdService = async (student: Student): Promise<Student> => {
    const token = localStorage.getItem("accessToken");
    const API_URL = "http://localhost:8080";
    const response = await fetch(`${API_URL}/api/v1/admin/student/${student.id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(student),
    });
    if (!response.ok) {
        throw new Error(`Błąd: ${response.status}`);
    }
    const data = await response.json();
    return data;
}

export const deleteStudentByIdService = async (id: string): Promise<void> => {
    const token = localStorage.getItem("accessToken");
    const API_URL = "http://localhost:8080";
    const response = await fetch(`${API_URL}/api/v1/admin/student/${id}`, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
    });
    if (!response.ok) {
        throw new Error(`Błąd: ${response.status}`);
    }

    const data = await response.json();
    return data;
};