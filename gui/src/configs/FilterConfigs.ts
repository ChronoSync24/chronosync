import { FilterField } from '../components/Filters';

/**
 * Configuration for the appointment type filter fields.
 *
 * @type {FilterField[]}
 */
export const appointmentTypeFilterFields: FilterField[] = [
  {
    key: 'name',
    label: 'Name',
    type: 'text',
    placeholder: 'Search by name...',
  },
];
