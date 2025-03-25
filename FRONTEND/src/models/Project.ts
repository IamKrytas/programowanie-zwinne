export interface Project {
    id: number;
    fileId: number[];
    studentId: number[];
    teacherId: number;
    tasks: number[];
    students: number[];
    name: string;
    description: string;
    doneDate: string;
    creationDate: string;
}