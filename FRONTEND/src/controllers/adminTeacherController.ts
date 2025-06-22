import { Teacher } from "../models/Teacher";
import { getAllTeachersService, getTeacherByIdService, createTeacherService, modifyTeacherByIdService, deleteTeacherByIdService } from "../services/adminTeacherService";

export const getAllTeachers = async (): Promise<Teacher[]> => {
    return await getAllTeachersService();
}

export const getTeacherById = async (id: string): Promise<Teacher> => {
    return await getTeacherByIdService(id);
}

export const createTeacher = async (teacher: Teacher): Promise<Teacher> => {
    return await createTeacherService(teacher);
}

export const modifyTeacher = async (teacher: Teacher): Promise<Teacher> => {
    return await modifyTeacherByIdService(teacher);
}

export const deleteTeacherById = async (id: string): Promise<void> => {
    return await deleteTeacherByIdService(id);
}