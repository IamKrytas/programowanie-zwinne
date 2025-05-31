import { registerUserService, loginUserService } from "../services/userService";
import { Student } from "../models/Student";
import { LoginCredentials } from "../models/auth/LoginCredentials.ts";
import JwtTokenPair from "../models/auth/JwtTokenPair.ts";

// Function to register a new user
export const registerUser = async (userData: Student): Promise<string> => {
  return await registerUserService(userData);
};

export const loginUser = async (userData: LoginCredentials): Promise<JwtTokenPair> => {
  return await loginUserService(userData);
};
