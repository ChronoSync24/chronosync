import { OptionType } from '../SelectField';

/**
 * Interface for field type.
 *
 * @param {'text'} text - Text input field
 * @param {'number'} number - Number input field
 * @param {'select'} select - Select input field
 * @param {'color'} color - Color input field
 */
export type FieldType = 'text' | 'number' | 'select' | 'color';

/**
 * Interface for field validation rules.
 *
 * @param {boolean} [required] - Whether the field is required
 * @param {number} [minLength] - Minimum length of the field value
 * @param {number} [maxLength] - Maximum length of the field value
 * @param {string} [pattern] - Regular expression pattern for validation
 * @param {(value: string) => string | null} [custom] - Custom validation function
 */
export type FieldValidation = {
  required?: boolean;
  minLength?: number;
  maxLength?: number;
  pattern?: string;
  custom?: (value: string) => string | null;
};

/**
 * Interface for asynchronous options result.
 *
 * @param {string[]} options - Array of options
 * @param {boolean} hasMore - Whether there are more options to load
 */
export type AsyncOptionsResult = {
  options: string[];
  hasMore: boolean;
};

/**
 * Interface for field configuration.
 *
 * @param {string} name - The name of the field
 * @param {string} [label] - The label to display for the field
 * @param {string} placeholder - The placeholder text to display when no value is selected
 * @param {FieldType} type - The type of the field
 * @param {OptionType[]} [options] - Array of options to display in the dropdown
 * @param {(input: string, page: number) => Promise<AsyncOptionsResult>} [asyncOptions] - Optional function to load options asynchronously
 * @param {FieldValidation} [validation] - Validation rules for the field
 * @param {any} [value] - The current value of the field
 */
export type FieldConfig = {
  name: string;
  label?: string;
  placeholder: string;
  type: FieldType;
  options?: OptionType[];
  asyncOptions?: (input: string, page: number) => Promise<AsyncOptionsResult>;
  validation?: FieldValidation;
  value?: any;
};

// Client Form Fields
// export const clientFormFields: FieldConfig[] = [
//   {
//     name: 'firstName',
//     label: 'First Name',
//     placeholder: 'Enter first name',
//     type: 'text',
//     validation: { required: true },
//   },
//   {
//     name: 'lastName',
//     label: 'Last Name',
//     placeholder: 'Enter last name',
//     type: 'text',
//     validation: { required: true },
//   },
//   {
//     name: 'email',
//     label: 'Email',
//     placeholder: 'Enter email',
//     type: 'text',
//     validation: { required: true },
//   },
//   {
//     name: 'phone',
//     label: 'Phone',
//     placeholder: 'Enter phone',
//     type: 'text',
//     validation: { required: true },
//   },
// ];

// // User Form Fields
// export const userFormFields: FieldConfig[] = [
//   {
//     name: 'name',
//     label: 'Name',
//     placeholder: 'Enter name',
//     type: 'text',
//     validation: { required: true },
//   },
//   {
//     name: 'email',
//     label: 'Email',
//     placeholder: 'Enter email',
//     type: 'text',
//     validation: { required: true },
//   },
//   {
//     name: 'role',
//     label: 'Role',
//     placeholder: 'Select role',
//     type: 'select',
//     options: ['admin', 'user', 'moderator'],
//     validation: { required: true },
//   },
//   {
//     name: 'department',
//     label: 'Department',
//     placeholder: 'Select department',
//     type: 'select',
//     options: ['IT', 'HR', 'Marketing', 'Finance', 'Sales'],
//     validation: { required: true },
//   },
//   {
//     name: 'status',
//     label: 'Status',
//     placeholder: 'Select status',
//     type: 'select',
//     options: ['active', 'inactive', 'pending', 'suspended'],
//     validation: { required: true },
//   },
//   {
//     name: 'location',
//     label: 'Location',
//     placeholder: 'Select location',
//     type: 'select',
//     options: [
//       'New York',
//       'Los Angeles',
//       'Chicago',
//       'Miami',
//       'Seattle',
//       'Boston',
//     ],
//     validation: { required: true },
//   },
// ];
