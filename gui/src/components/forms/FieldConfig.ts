import { OptionType } from '../SelectField';

export type FieldType = 'text' | 'number' | 'select' | 'color';

export type FieldValidation = {
  required?: boolean;
  minLength?: number;
  maxLength?: number;
  pattern?: string;
  custom?: (value: string) => string | null;
};

export type AsyncOptionsResult = {
  options: string[];
  hasMore: boolean;
};

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
