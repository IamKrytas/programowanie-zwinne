import { registerUserService, loginUserService } from "../services/userService";
import { RegisterFormData, LoginFormData } from "../models/User";

// Function to register a new user
export const registerUser = async (userData: RegisterFormData): Promise<string> => {
  return await registerUserService(userData);
};

export const loginUser = async (userData: LoginFormData): Promise<string> => {
  return await loginUserService(userData);
};
