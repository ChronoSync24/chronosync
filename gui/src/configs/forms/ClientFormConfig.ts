import { FieldConfig } from '../../components/forms/FieldConfig';

/**
 * Configuration for the client form fields.
 *
 * @type {FieldConfig[]}
 */
export const clientFormFields: FieldConfig[] = [
  {
    name: 'firstName',
    label: 'First Name',
    placeholder: 'Enter first name',
    type: 'text',
    validation: {
      required: true,
      maxLength: 50,
      minLength: 3,
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
      minLength: 3,
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
];
