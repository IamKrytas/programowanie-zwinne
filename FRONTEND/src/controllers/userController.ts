import { registerUserService } from "../services/userService";
import { RegisterFormData } from "../models/User";

// Function to register a new user
export const registerUser = async (userData: RegisterFormData): Promise<string> => {
  return await registerUserService(userData);
};
