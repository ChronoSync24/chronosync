import { FieldConfig } from '../../components/forms/FieldConfig';
import { UserRole } from '../../models/user/UserRole';

/**
 * Configuration for the user form fields.
 *
 * @type {FieldConfig[]}
 */
export const userFormFields: FieldConfig[] = [
  {
    name: 'firstName',
    label: 'First Name',
    placeholder: 'Enter first name',
    type: 'text',
    validation: {
      required: true,
      maxLength: 50,
      minLength: 2,
      pattern: '^[a-zA-Z]+$',
    },
  },
  {
    name: 'lastName',
    label: 'Last Name',
    placeholder: 'Enter last name',
    type: 'text',
    validation: {
      required: true,
      maxLength: 50,
      minLength: 2,
      pattern: '^[a-zA-Z]+$',
    },
  },
  {
    name: 'email',
    label: 'Email',
    placeholder: 'Enter email address',
    type: 'text',
    validation: {
      required: true,
      maxLength: 100,
      minLength: 5,
      pattern: '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$',
    },
  },
  {
    name: 'phone',
    label: 'Phone',
    placeholder: 'Enter phone number',
    type: 'text',
    validation: {
      required: true,
      maxLength: 20,
      minLength: 5,
      pattern: '^\\+\\d+$',
    },
  },
  {
    name: 'uniqueIdentifier',
    label: 'Unique Identifier',
    placeholder: 'Enter unique identifier',
    type: 'text',
    validation: {
      required: true,
      maxLength: 13,
      minLength: 13,
      pattern: '^[0-9]+$',
    },
  },
  {
    name: 'address',
    label: 'Address',
    placeholder: 'Enter address',
    type: 'text',
    validation: {
      required: true,
      maxLength: 120,
      minLength: 5,
    },
  },
  {
    name: 'password',
    label: 'Password',
    placeholder: 'Enter password',
    type: 'text',
    showOnCreate: true,
    validation: {
      required: true,
      maxLength: 100,
      minLength: 6,
    },
  },
  {
    name: 'role',
    label: 'Role',
    placeholder: 'Select role',
    type: 'select',
    options: [
      { value: UserRole.ADMIN, label: 'Administrator' },
      { value: UserRole.MANAGER, label: 'Manager' },
      { value: UserRole.EMPLOYEE, label: 'Employee' },
    ],
    validation: {
      required: true,
    },
  },
  {
    name: 'isEnabled',
    label: 'Account Enabled',
    placeholder: '',
    type: 'checkbox',
    validation: {
      required: false,
    },
  },
];
