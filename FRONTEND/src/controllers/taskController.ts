import { Task } from "../models/Task";
import { getAllTasksService, getTaskByIdService, createTaskService, modifyTaskByIdService, deleteTaskByIdService  } from "../services/taskService";

export const getAllTasks = async (): Promise<Task[]> => {
    return await getAllTasksService();
}

export const getTaskById = async (id: string): Promise<Task> => {
    return await getTaskByIdService(id);
}

export const createTask = async (task: Task): Promise<Task> => {
    return await createTaskService(task);
}

export const modifyTask = async (task: Task): Promise<Task> => {
    return await modifyTaskByIdService(task);
}

export const deleteTask = async (id: string): Promise<void> => {
    return await deleteTaskByIdService(id);
}
