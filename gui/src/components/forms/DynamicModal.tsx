import React, { useState, useEffect } from 'react';
import TextInput from './TextInput';
import PrimaryButton from '../PrimaryButton';
import SecondaryButton from '../SecondaryButton';
import SelectField from '../SelectField';
import './DynamicModal.css';
import { CircularProgress, Dialog, DialogProps, Box } from '@mui/material';
import { FieldConfig } from './FieldConfig';

/**
 * Props for the DynamicModal component.
 *
 * @param {string} title - The title of the modal
 * @param {FieldConfig[]} fields - The fields to display in the modal
 * @param {Record<string, string>} [initialValues] - The initial values for the fields
 * @param {(formData: Record<string, string>) => void} onSubmit - The function to call when the form is submitted
 * @param {() => void} [onCancel] - The function to call when the form is cancelled
 * @param {boolean} open - Whether the modal is open
 * @param {() => void} onClose - The function to call when the modal is closed
 * @param {DialogProps['maxWidth']} [maxWidth] - The maximum width of the modal
 * @param {boolean} [isLoading] - Whether the modal is loading
 * @param {boolean} [isCreate] - Whether the modal is creating a new record
 */
type DynamicModalProps = {
  title: string;
  fields: FieldConfig[];
  initialValues?: Record<string, string>;
  onSubmit: (formData: Record<string, string>) => void;
  onCancel?: () => void;
  open: boolean;
  onClose: () => void;
  maxWidth?: DialogProps['maxWidth'];
  isLoading?: boolean;
  isCreate?: boolean;
};

/**
 * A reusable modal component with dynamic form fields and validation.
 * Supports text, number, and select inputs, with optional initial values,
 * validation rules, and asynchronous option loading.
 */
export default function DynamicModal({
  title,
  fields,
  initialValues = {},
  onSubmit,
  onCancel,
  open,
  onClose,
  maxWidth = 'sm',
  isLoading = false,
  isCreate = false,
}: DynamicModalProps) {
  const [form, setForm] = useState<Record<string, string>>(() => {
    const defaultValues: Record<string, string> = {};
    fields.forEach((field: FieldConfig) => {
      defaultValues[field.name] = initialValues[field.name] || '';
    });

    return defaultValues;
  });

  const [errors, setErrors] = useState<Record<string, string>>({});
  const [touched, setTouched] = useState<Record<string, boolean>>({});

  useEffect(() => {
    const defaultValues: Record<string, string> = {};
    fields.forEach((field: FieldConfig) => {
      defaultValues[field.name] = initialValues[field.name] || '';
    });
    setForm(defaultValues);
    setErrors({});
    setTouched({});
  }, [initialValues, fields]);

  const handleChange = (name: string, value: string) => {
    setForm((prev) => ({ ...prev, [name]: value }));
    setErrors((prev) => ({ ...prev, [name]: '' }));
  };

  const handleReset = () => {
    const resetValues = fields.reduce(
      (acc: Record<string, string>, field: FieldConfig) => {
        acc[field.name] = initialValues[field.name] || '';
        return acc;
      },
      {}
    );
    setForm(resetValues);
    setErrors({});
    setTouched({});
  };

  const validate = () => {
    const newErrors: Record<string, string> = {};
    fields.forEach((field) => {
      if (field.validation?.required && !form[field.name]) {
        newErrors[field.name] = 'Required';
      }
      if (
        field.validation?.maxLength &&
        form[field.name] &&
        form[field.name].length > field.validation.maxLength
      ) {
        newErrors[field.name] = `Max ${field.validation.maxLength} chars`;
      }
      if (
        field.validation?.minLength &&
        form[field.name] &&
        form[field.name].length < field.validation.minLength
      ) {
        newErrors[field.name] = `Min ${field.validation.minLength} chars`;
      }
      if (
        field.validation?.pattern &&
        form[field.name] &&
        !new RegExp(field.validation.pattern).test(form[field.name])
      ) {
        newErrors[field.name] = 'Invalid pattern';
      }
    });
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (validate()) {
      onSubmit(form);
      handleReset();
    } else {
      setTouched(
        fields.reduce(
          (acc, field) => {
            acc[field.name] = true;
            return acc;
          },
          {} as Record<string, boolean>
        )
      );
    }
  };

  const handleCancel = () => {
    if (onCancel) {
      onCancel();
    } else {
      handleReset();
    }
  };

  const renderField = (field: FieldConfig) => {
    const value = form[field.name];
    const error = touched[field.name] && errors[field.name];

    // Select input field
    if (field.type === 'select' && field.options) {
      return (
        <div key={field.name} className='dynamic-form-field'>
          <SelectField
            label={field.label || field.name}
            placeholder={field.placeholder}
            value={value}
            onChange={(newValue) => handleChange(field.name, String(newValue))}
            options={field.options}
            error={!!error}
            helperText={error || undefined}
            className='dynamic-input'
          />
        </div>
      );
    }

    // Text/number/color input fields
    return (
      <div key={field.name} className='dynamic-form-field'>
        <TextInput
          type={field.type}
          placeholder={field.placeholder}
          label={field.label}
          value={value}
          onChange={(e) => handleChange(field.name, e.target.value)}
          className='dynamic-input'
          slotProps={{
            htmlInput: {
              maxLength: field.validation?.maxLength || 50,
            },
          }}
          error={!!error}
          helperText={error}
        />
      </div>
    );
  };

  return (
    <Dialog open={open} onClose={onClose} maxWidth={maxWidth} fullWidth>
      <form onSubmit={handleSubmit} className='dynamic-form' noValidate>
        <div className='modal-header'>
          <h2 className='dynamic-form-title'>{title}</h2>
        </div>
        {isLoading ?
          <Box
            sx={{
              display: 'flex',
              justifyContent: 'center',
              alignItems: 'center',
              minHeight: '120px',
            }}>
            <CircularProgress />
          </Box>
        : <>
            <div className='modal-body'>{fields.map(renderField)}</div>
            <div className='modal-footer dynamic-form-buttons'>
              <SecondaryButton
                type='button'
                onClick={handleCancel}
                className='dynamic-button-cancel'>
                Cancel
              </SecondaryButton>
              <PrimaryButton type='submit' className='dynamic-button-submit'>
                {isCreate ? 'Create' : 'Update'}
              </PrimaryButton>
            </div>
          </>
        }
      </form>
    </Dialog>
  );
}
