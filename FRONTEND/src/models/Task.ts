export interface Task {
    id: number;
    fileId: number[];
    studentId: number;
    projectId: number;
    name: string;
    description: string;
    priority: number;
    doneDate: Date;
    creationDate: Date;
}