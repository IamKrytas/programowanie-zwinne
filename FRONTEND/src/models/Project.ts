import {Task} from "./Task.ts";

export interface Project {
    id: number;
    fileId: number[];
    studentId: number[];
    teacherId: number;
    tasks: Task[];
    students: number[];
    name: string;
    description: string;
    doneDate: Date;
    creationDate: Date;
}