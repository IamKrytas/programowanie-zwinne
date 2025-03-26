import { RegisterFormData, LoginFormData } from "../models/User";

// Register new user
export const registerUserService = async (userData: RegisterFormData): Promise<string> => {
  const API_URL = "http://localhost:5000";
  const response = await fetch(`${API_URL}/register`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(userData),
  });
  return await response.json();
}

// Login existing user
export const loginUserService = async (userData: LoginFormData): Promise<string> => {
// ...
}