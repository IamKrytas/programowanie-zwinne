import { Project } from "../models/Project";

export const getAllProjectsService = async (): Promise<Project[]> => {
    const token = sessionStorage.getItem("accessToken");
    const API_URL = "http://localhost:8080";
    const response = await fetch(`${API_URL}/api/v1/project?offset=0&limit=10`, {
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

export const getProjectByIdService = async (id: string): Promise<Project> => {
    const token = sessionStorage.getItem("accessToken");
    const API_URL = "http://localhost:8080";
    const respone = await fetch(`${API_URL}/api/v1/project/${id}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
    });
    if (!respone.ok) {
        throw new Error(`Błąd: ${respone.status}`);
    }
    const data = await respone.json();
    return data;
}


export const createProjectService = async (project: Project): Promise<Project> => {
    const token = sessionStorage.getItem("accessToken");
    const API_URL = "http://localhost:8080";
    const response = await fetch(`${API_URL}/api/v1/project`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(project),
    });

    if (!response.ok) {
        throw new Error(`Błąd: ${response.status}`);
    }
    const data = await response.json();
    return data;
}

export const modifyProjectByIdService = async (project: Project): Promise<Project> => {
    const token = sessionStorage.getItem("accessToken");
    const API_URL = "http://localhost:8080";
    const response = await fetch(`${API_URL}/api/v1/project/${project.id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(project),
    });

    if (!response.ok) {
        throw new Error(`Błąd: ${response.status}`);
    }
    const data = await response.json();
    return data;
}

export const deleteProjectByIdService = async (id: string): Promise<void> => {
    const token = sessionStorage.getItem("accessToken");
    const API_URL = "http://localhost:8080";
    const response = await fetch(`${API_URL}/api/v1/project/${id}`, {
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
