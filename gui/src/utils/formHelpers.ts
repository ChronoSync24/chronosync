import { Client } from '../models/Client';
import { FieldConfig } from '../components/forms/FieldConfig';

/**
 * Extracts initial values for a form from an entity object based on field configuration.
 *
 * @param {any} entity - The entity object to extract values from
 * @param {FieldConfig[]} fields - Array of field configurations
 * @returns {Record<string, string>} Form initial values
 */
export const extractFormValues = (
  entity: any,
  fields: FieldConfig[]
): Record<string, string> => {
  if (!entity) return {};

  const initialValues: Record<string, string> = {};

  fields.forEach((field) => {
    const value = entity[field.name];
    if (value !== undefined && value !== null) {
      initialValues[field.name] = String(value);
    }
  });

  return initialValues;
};
