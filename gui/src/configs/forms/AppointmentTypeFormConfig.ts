import { FieldConfig } from '../../components/forms/FieldConfig';
import { OptionType } from '../../components/SelectField';

/**
 * Options for the currency field.
 *
 * @type {OptionType[]}
 */
const currencyOptions: OptionType[] = [
  { value: 'EUR', label: 'EUR' },
  { value: 'CHF', label: 'CHF' },
  { value: 'GBP', label: 'GBP' },
  { value: 'BAM', label: 'BAM' },
  { value: 'USD', label: 'USD' },
];

/**
 * Configuration for the appointment type form fields.
 *
 * @type {FieldConfig[]}
 */
export const appointmentTypeFormFields: FieldConfig[] = [
  {
    name: 'name',
    label: 'Appointment Name',
    placeholder: 'Enter appointment type name',
    type: 'text',
    validation: { required: true, maxLength: 50 },
  },
  {
    name: 'durationMinutes',
    label: 'Duration (minutes)',
    placeholder: 'Enter duration in minutes',
    type: 'number',
    validation: { required: true },
  },
  {
    name: 'price',
    label: 'Price',
    placeholder: 'Enter price',
    type: 'number',
    validation: { required: true },
  },
  {
    name: 'currency',
    label: 'Currency',
    placeholder: 'Select currency',
    type: 'select',
    options: currencyOptions,
    validation: { required: true },
  },
  {
    name: 'colorCode',
    label: 'Color',
    placeholder: 'Pick a color',
    type: 'color',
    validation: { required: true },
  },
];
