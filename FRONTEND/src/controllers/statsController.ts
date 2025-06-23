import Stats from "../models/Stats.ts";
import {getAllStats} from "../services/statsService.ts";


export const getStats = async (): Promise<Stats> => {
    return await getAllStats();
}
