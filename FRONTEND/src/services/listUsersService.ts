import {Teacher} from "../models/Teacher.ts";
import {Student} from "../models/Student.ts";

export const listAllStudentsService = async (): Promise<Student[]> => {
    const token = localStorage.getItem("accessToken");
    const API_URL = "http://localhost:8080";
    const response = await fetch(`${API_URL}/api/v1/list/students`, {
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

export const listAllTeachersService = async (): Promise<Teacher[]> => {
    const token = localStorage.getItem("accessToken");
    const API_URL = "http://localhost:8080";
    const response = await fetch(`${API_URL}/api/v1/list/teachers`, {
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
