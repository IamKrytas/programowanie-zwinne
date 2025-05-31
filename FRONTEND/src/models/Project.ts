import {Task} from "./Task.ts";

export interface Project {
    id: string;
    fileIds: string[];
    teacherId: string;
    tasks: Task[];
    studentIds: string[];
    name: string;
    description: string;
    doneDate: Date;
    creationDate: Date;
}
