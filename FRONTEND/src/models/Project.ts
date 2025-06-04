export interface Project {
    id: string;
    fileIds: string[];
    teacherId: string;
    taskIds: string[];
    studentIds: string[];
    name: string;
    description: string;
    doneDate: Date;
    creationDate: Date;
}
