import { User } from "../models/User";

// Login exist user
export const loginUser = async (): Promise<User> => {
  const API_URL = "localhost:5000"; // dummy, to be changed later
  const response = await fetch(`${API_URL}/login`);
  return response.json();
};

// Register new user
export const registerUser = async (): Promise<User> => {
  const API_URL = "localhost:5000"; // dummy, to be changed later
  const response = await fetch(`${API_URL}/register`);
  return response.json();
}

export const fetchUsers = async (): Promise<User[]> => {
  const API_URL = "localhost:5000"; // dummy, to be changed later
  const response = await fetch(`${API_URL}/users`);
  return response.json();
}
