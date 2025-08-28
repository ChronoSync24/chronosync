import { OptionType } from '../SelectField';

/**
 * Interface for field type.
 *
 * @param {'text'} text - Text input field
 * @param {'number'} number - Number input field
 * @param {'select'} select - Select input field
 * @param {'color'} color - Color input field
 * @param {'checkbox'} checkbox - Checkbox input field
 */
export type FieldType = 'text' | 'number' | 'select' | 'color' | 'checkbox';

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
 * @param {boolean} [showOnCreate] - Whether the field should only be shown when creating (not updating)
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
  showOnCreate?: boolean;
};
