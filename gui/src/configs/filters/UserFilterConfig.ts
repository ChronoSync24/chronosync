import { FilterField } from '../../components/Filters';
import { UserRole } from '../../models/user/UserRole';

/**
 * Configuration for the user filter fields.
 *
 * @type {FilterField[]}
 */
export const userFilterFields: FilterField[] = [
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
    key: 'username',
    label: 'Username',
    type: 'text',
    placeholder: 'Search by username...',
  },
  {
    key: 'uniqueIdentifier',
    label: 'Unique Identifier',
    type: 'text',
    placeholder: 'Search by unique identifier...',
  },
  {
    key: 'role',
    label: 'Role',
    type: 'select',
    options: [
      { value: UserRole.ADMIN, label: 'Administrator' },
      { value: UserRole.MANAGER, label: 'Manager' },
      { value: UserRole.EMPLOYEE, label: 'Employee' },
    ],
    accessibleBy: [UserRole.ADMIN],
  },
];
