import {Task} from "./Task.ts";

export interface Project {
    id: string;
    fileId: string[];
    studentId: string[];
    teacherId: string;
    tasks: Task[];
    students: number[];
    name: string;
    description: string;
    doneDate: Date;
    creationDate: Date;
}