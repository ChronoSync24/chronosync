import { FilterField } from '../../components/Filters';

/**
 * Configuration for the client filter fields.
 *
 * @type {FilterField[]}
 */
export const clientFilterFields: FilterField[] = [
  {
    key: 'firstName',
    label: 'First Name',
    type: 'text',
    placeholder: 'Search by first name...',
  },
  {
    key: 'lastName',
    label: 'Last Name',
    type: 'text',
    placeholder: 'Search by last name...',
  },
  {
    key: 'email',
    label: 'Email',
    type: 'text',
    placeholder: 'Search by email...',
  },
  {
    key: 'phone',
    label: 'Phone',
    type: 'text',
    placeholder: 'Search by phone...',
  },
  {
    key: 'uniqueIdentifier',
    label: 'Unique Identifier',
    type: 'text',
    placeholder: 'Search by unique identifier...',
  },
];
