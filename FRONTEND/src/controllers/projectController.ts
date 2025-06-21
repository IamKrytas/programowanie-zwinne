import { Project } from '../models/Project';
import { getAllProjectsService, getProjectByIdService, createProjectService, modifyProjectByIdService, deleteProjectByIdService } from '../services/projectService';

export const getAllProjects = async (): Promise<Project[]> => {
    return await getAllProjectsService();
}

export const getProjectById = async (id: string): Promise<Project> => {
    return await getProjectByIdService(id);
};

export const createProject = async (project: Project): Promise<Project> => {
    return await createProjectService(project);
}

export const modifyProject = async (project: Project): Promise<Project> => {
    return await modifyProjectByIdService(project);
};

export const deleteProject = async (id: string): Promise<void> => {
    return await deleteProjectByIdService(id);
}