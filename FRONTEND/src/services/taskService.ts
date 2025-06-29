import { Task } from "../models/Task";

export const getAllTasksService = async (): Promise<Task[]> => {
    const token = localStorage.getItem("accessToken");
    const API_URL = "http://localhost:8080";
    const response = await fetch(`${API_URL}/api/v1/task?offset=0&limit=10`, {
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

export const getTaskByIdService = async (id: string): Promise<Task> => {
    const token = localStorage.getItem("accessToken");
    const API_URL = "http://localhost:8080";
    const response = await fetch(`${API_URL}/api/v1/task/${id}`, {
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

export const createTaskService = async (task: Task): Promise<Task> => {
    const token = localStorage.getItem("accessToken");
    const API_URL = "http://localhost:8080";
    const response = await fetch(`${API_URL}/api/v1/task/${task.projectId}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(task),
    });
    if (!response.ok) {
        throw new Error(`Błąd: ${response.status}`);
    }
    const data = await response.json(); 
    return data;
}

export const modifyTaskByIdService = async (task: Task): Promise<Task> => {
    const token = localStorage.getItem("accessToken");
    const API_URL = "http://localhost:8080";
    const response = await fetch(`${API_URL}/api/v1/task/${task.id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(task),
    });
    if (!response.ok) {
        throw new Error(`Błąd: ${response.status}`);
    }
    const data = await response.json();
    return data;
}

export const deleteTaskByIdService = async (id: string): Promise<void> => {
    const token = localStorage.getItem("accessToken");
    const API_URL = "http://localhost:8080";
    const response = await fetch(`${API_URL}/api/v1/task/${id}`, {
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