export interface Task {
    id: string;
    projectId: string;
    teacherId: string;
    fileIds: string[];
    assignedStudentId: number;
    name: string;
    description: string;
    priority: number;
    doneDate: Date;
    creationDate: Date;
}
