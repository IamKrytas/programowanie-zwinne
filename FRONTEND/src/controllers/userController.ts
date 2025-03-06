import { fetchUsers } from "../services/userService";
import { User } from "../models/User";

export const getUsers = async (): Promise<User[]> => {
  return await fetchUsers();
};
