import {Student} from "../models/Student.ts";
import {Teacher} from "../models/Teacher.ts";
import {getAllStudentsService} from "../services/adminStudentService.ts";
import {getAllTeachersService} from "../services/adminTeacherService.ts";

export const getAllStudents = async (): Promise<Student[]> => {
    return getAllStudentsService();
}

export const getAllTeachers = async (): Promise<Teacher[]> => {
    return getAllTeachersService();
}