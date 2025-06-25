export interface Task {
    id: string;
    projectId: string;
    teacherId: string;
    fileIds: string[];
    assignedStudentId: string | null;
    name: string;
    description: string;
    priority: number;
    doneDate: Date | null;
    creationDate: Date | null;
}
