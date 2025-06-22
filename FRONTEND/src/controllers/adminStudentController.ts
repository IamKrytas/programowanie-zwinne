import { Student } from "../models/Student";
import { getAllStudentsService, getStudentByIdService, createStudentService, modifyStudentByIdService, deleteStudentByIdService } from "../services/adminStudentService";

export const getAllStudents = async (): Promise<Student[]> => {
    return await getAllStudentsService();
}

export const getStudentById = async (id: string): Promise<Student> => {
    return await getStudentByIdService(id);
}

export const createStudent = async (student: Student): Promise<Student> => {
    return await createStudentService(student);
}

export const modifyStudent = async (student: Student): Promise<Student> => {
    return await modifyStudentByIdService(student);
}

export const deleteStudentById = async (id: string): Promise<void> => {
    return await deleteStudentByIdService(id);
}
