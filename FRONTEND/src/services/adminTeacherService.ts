import { Teacher } from "../models/Teacher";

export const getAllTeachersService = async (): Promise<Teacher[]> => {
    const token = sessionStorage.getItem("accessToken");
    const API_URL = "http://localhost:8080";
    const response = await fetch(`${API_URL}/api/v1/admin/teacher?offset=0&limit=10`, {
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

export const getTeacherByIdService = async (id: string): Promise<Teacher> => {
    const token = sessionStorage.getItem("accessToken");
    const API_URL = "http://localhost:8080";
    const response = await fetch(`${API_URL}/api/v1/admin/teacher/${id}`, {
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

export const createTeacherService = async (teacher: Teacher): Promise<Teacher> => {
    const token = sessionStorage.getItem("accessToken");
    const API_URL = "http://localhost:8080";
    const response = await fetch(`${API_URL}/api/v1/admin/teacher`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(teacher),
    });

    if (!response.ok) {
        throw new Error(`Błąd: ${response.status}`);
    }

    const data = await response.json();
    return data;
}

export const modifyTeacherByIdService = async (teacher: Teacher): Promise<Teacher> => {
    const token = sessionStorage.getItem("accessToken");
    const API_URL = "http://localhost:8080";
    const response = await fetch(`${API_URL}/api/v1/admin/teacher/${teacher.id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(teacher),
    });

    if (!response.ok) {
        throw new Error(`Błąd: ${response.status}`);
    }

    const data = await response.json();
    return data;
}

export const deleteTeacherByIdService = async (id: string): Promise<void> => {
    const token = sessionStorage.getItem("accessToken");
    const API_URL = "http://localhost:8080";
    const response = await fetch(`${API_URL}/api/v1/admin/teacher/${id}`, {
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
}