import { BaseEntity } from './BaseEntity';
import { Firm } from './Firm';
import { User } from "./user/User";
import {AppointmentType} from "./appointmentType/AppointmentType";
import { Client } from "./Client";

/**
 * Appointment type model interface.
 */
export interface Appointment extends BaseEntity {
    note: string;
    startDateTime: string;
    endDateTime: string;
    employee?: User;
    client?:  Client;
    appointmentType?: AppointmentType;
    firm?: Firm;
}
