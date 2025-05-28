export interface Task {
    id: string;
    fileId: string[];
    studentId: number;
    projectId: number;
    name: string;
    description: string;
    priority: number;
    doneDate: Date;
    creationDate: Date;
}