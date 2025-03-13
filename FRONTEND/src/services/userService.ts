import { User } from "../models/User";

// Login exist user
export const loginUser = async (): Promise<User> => {
  const API_URL = import.meta.env.VITE_API_URL;
  const response = await fetch(`${API_URL}/login`);
  return response.json();
};

// Register new user
export const registerUser = async (): Promise<User> => {
  const API_URL = import.meta.env.VITE_API_URL;
  const response = await fetch(`${API_URL}/register`);
  return response.json();
}

