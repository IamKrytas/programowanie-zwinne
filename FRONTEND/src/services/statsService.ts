import Stats from "../models/Stats.ts";

export const getAllStats = async (): Promise<Stats> => {
    const API_URL = "http://localhost:8080";
    const response = await fetch(`${API_URL}/api/v1/stats`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
        },
    });

    if (!response.ok) {
        throw new Error(`Błąd: ${response.status}`);
    }

    const data = await response.json();
    return data;
}
