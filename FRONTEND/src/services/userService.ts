import { Student } from "../models/Student";
import { User } from "../models/User";

// Register new user
export const registerUserService = async (userData: Student): Promise<string> => {
  const API_URL = "http://localhost:5000";
  const response = await fetch(`${API_URL}/api/v1/auth/register/student`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(userData),
  });
  return await response.json();
}

// Login existing user
export const loginUserService = async (userData: User): Promise<string> => {
  const API_URL = "http://localhost:5000";
  const response = await fetch(`${API_URL}/api/v1/auth/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(userData),
  });
  return await response.json();
}