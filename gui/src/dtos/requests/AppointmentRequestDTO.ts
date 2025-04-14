import { Firm } from '../../models/Firm';
import {User} from "../../models/user/User";
import {Client} from "../../models/Client";
import {AppointmentType} from "../../models/appointmentType/AppointmentType";

/**
 * Appointment request DTO.
 */
export interface AppointmentRequestDTO {
    id: number;
    note: string;
    startDateTime: string;
    endDateTime: string;
    employee: User;
    client:  Client;
    appointmentType?: AppointmentType;
    firm: Firm;
}
