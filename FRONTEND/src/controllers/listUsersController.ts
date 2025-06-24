import {Student} from "../models/Student.ts";
import {Teacher} from "../models/Teacher.ts";
import {listAllStudentsService,  listAllTeachersService} from "../services/listUsersService.ts";

export const getAllStudents = async (): Promise<Student[]> => {
    return listAllStudentsService();
}

export const getAllTeachers = async (): Promise<Teacher[]> => {
    return listAllTeachersService();
}