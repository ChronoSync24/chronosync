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
  options?: string[];
  asyncOptions?: (input: string, page: number) => Promise<AsyncOptionsResult>;
  validation?: FieldValidation;
};

// Appointment Type Form Fields
export const appointmentTypeFormFields: FieldConfig[] = [
  {
    name: 'name',
    label: 'Appointment Name',
    placeholder: 'Enter appointment type',
    type: 'text',
    validation: { required: true, maxLength: 50 },
  },
  {
    name: 'duration',
    label: 'Duration (minutes)',
    placeholder: 'Enter duration',
    type: 'number',
    validation: { required: true },
  },
  { name: 'price', label: 'Price', placeholder: 'Enter price', type: 'number' },
  {
    name: 'currency',
    label: 'Currency',
    placeholder: 'Select currency',
    type: 'select',
    options: ['USD', 'EUR', 'GBP'],
    validation: { required: true },
  },
  { name: 'color', label: 'Color', placeholder: 'Pick a color', type: 'color' },
  {
    name: 'city',
    label: 'City',
    placeholder: 'Type to search cities',
    type: 'select',
    asyncOptions: async (input, page) => {
      const allCities = [
        'Tuzla',
        'Zivinice',
        'Chicago',
        'LA',
        'Maribor',
        'Ljubljana',
        'Amorim',
        'cr7',
        'mavs',
        'Pozz',
        'Sarajevo',
        'Srebrenik',
        'Sladna',
        'Slatina',
        'Slavonski Brod',
        'Slavonski Brod',
        'Slavonski Brod',
        'Slavonski Brod',
        'Slavonski Brod',
        'Slavonski Brod',
        'Slavonski Brod',
        'Slavonski Brod',
        'Silueta',
        'Sisa',
        'Sarma',
        'Stepenica',
        'Sikira',
      ];
      const filtered = allCities.filter((city) =>
        city.toLowerCase().includes(input.toLowerCase())
      );
      const pageSize = 5;
      const paged = filtered.slice((page - 1) * pageSize, page * pageSize);
      await new Promise((res) => setTimeout(res, 500));
      return { options: paged, hasMore: filtered.length > page * pageSize };
    },
    validation: { required: true },
  },
];

// Client Form Fields
export const clientFormFields: FieldConfig[] = [
  {
    name: 'firstName',
    label: 'First Name',
    placeholder: 'Enter first name',
    type: 'text',
    validation: { required: true },
  },
  {
    name: 'lastName',
    label: 'Last Name',
    placeholder: 'Enter last name',
    type: 'text',
    validation: { required: true },
  },
  {
    name: 'email',
    label: 'Email',
    placeholder: 'Enter email',
    type: 'text',
    validation: { required: true },
  },
  {
    name: 'phone',
    label: 'Phone',
    placeholder: 'Enter phone',
    type: 'text',
    validation: { required: true },
  },
];

// User Form Fields
export const userFormFields: FieldConfig[] = [
  {
    name: 'name',
    label: 'Name',
    placeholder: 'Enter name',
    type: 'text',
    validation: { required: true },
  },
  {
    name: 'email',
    label: 'Email',
    placeholder: 'Enter email',
    type: 'text',
    validation: { required: true },
  },
  {
    name: 'role',
    label: 'Role',
    placeholder: 'Select role',
    type: 'select',
    options: ['admin', 'user', 'moderator'],
    validation: { required: true },
  },
  {
    name: 'department',
    label: 'Department',
    placeholder: 'Select department',
    type: 'select',
    options: ['IT', 'HR', 'Marketing', 'Finance', 'Sales'],
    validation: { required: true },
  },
  {
    name: 'status',
    label: 'Status',
    placeholder: 'Select status',
    type: 'select',
    options: ['active', 'inactive', 'pending', 'suspended'],
    validation: { required: true },
  },
  {
    name: 'location',
    label: 'Location',
    placeholder: 'Select location',
    type: 'select',
    options: [
      'New York',
      'Los Angeles',
      'Chicago',
      'Miami',
      'Seattle',
      'Boston',
    ],
    validation: { required: true },
  },
];
