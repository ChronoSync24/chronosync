import React, { useState, useEffect } from 'react';
import TextInput from './TextInput';
import PrimaryButton from '../PrimaryButton';
import SecondaryButton from '../SecondaryButton';
import SelectField from '../SelectField';
import './DynamicModal.css';
import {
  CircularProgress,
  Dialog,
  DialogProps,
  Box,
  Checkbox,
  FormControlLabel,
  FormHelperText,
} from '@mui/material';
import { FieldConfig } from './FieldConfig';

/**
 * Props for the DynamicModal component.
 *
 * @param {string} title - The title of the modal
 * @param {FieldConfig[]} fields - The fields to display in the modal
 * @param {Record<string, string | boolean>} [initialValues] - The initial values for the fields
 * @param {(formData: Record<string, string | boolean>) => void} onSubmit - The function to call when the form is submitted
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
  initialValues?: Record<string, string | boolean>;
  onSubmit: (formData: Record<string, string | boolean>) => void;
  onCancel?: () => void;
  open: boolean;
  onClose: () => void;
  maxWidth?: DialogProps['maxWidth'];
  isLoading?: boolean;
  isCreate?: boolean;
};

/**
 * A reusable modal component with dynamic form fields and validation.
 * Supports text, number, select, and checkbox inputs, with optional initial values,
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
  const [form, setForm] = useState<Record<string, string | boolean>>(() => {
    const defaultValues: Record<string, string | boolean> = {};
    fields.forEach((field: FieldConfig) => {
      if (field.type === 'checkbox') {
        defaultValues[field.name] = initialValues[field.name] ?? false;
      } else if (field.name === 'phone') {
        const initial = (initialValues[field.name] as string) || '';
        defaultValues[field.name] =
          isCreate ?
            initial.startsWith('+') ?
              initial
            : '+'
          : initial;
      } else {
        defaultValues[field.name] = (initialValues[field.name] as string) || '';
      }
    });

    return defaultValues;
  });

  const [errors, setErrors] = useState<Record<string, string>>({});
  const [touched, setTouched] = useState<Record<string, boolean>>({});

  useEffect(() => {
    const defaultValues: Record<string, string | boolean> = {};
    fields.forEach((field: FieldConfig) => {
      if (field.type === 'checkbox') {
        defaultValues[field.name] = initialValues[field.name] ?? false;
      } else if (field.name === 'phone') {
        const initial = (initialValues[field.name] as string) || '';
        defaultValues[field.name] =
          isCreate ?
            initial.startsWith('+') ?
              initial
            : '+'
          : initial;
      } else {
        defaultValues[field.name] = (initialValues[field.name] as string) || '';
      }
    });
    setForm(defaultValues);
    setErrors({});
    setTouched({});
  }, [initialValues, fields, isCreate]);

  const handleChange = (name: string, value: string | boolean) => {
    setForm((prev) => ({ ...prev, [name]: value }));
    setErrors((prev) => ({ ...prev, [name]: '' }));
  };

  const handleReset = () => {
    const resetValues = fields.reduce(
      (acc: Record<string, string | boolean>, field: FieldConfig) => {
        if (field.type === 'checkbox') {
          acc[field.name] = initialValues[field.name] ?? false;
        } else if (field.name === 'phone') {
          const initial = (initialValues[field.name] as string) || '';
          acc[field.name] =
            isCreate ?
              initial.startsWith('+') ?
                initial
              : '+'
            : initial;
        } else {
          acc[field.name] = (initialValues[field.name] as string) || '';
        }
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

    // Only validate fields that are actually visible
    const visibleFields = fields.filter(
      (field) => !field.showOnCreate || isCreate
    );

    visibleFields.forEach((field) => {
      const value = form[field.name];

      if (field.validation?.required) {
        if (field.type === 'checkbox' && value !== true) {
          newErrors[field.name] = 'Required';
        } else if (field.type !== 'checkbox' && !value) {
          newErrors[field.name] = 'Required';
        }
      }

      // String-specific validations
      if (field.type !== 'checkbox' && typeof value === 'string') {
        if (
          field.validation?.maxLength &&
          value &&
          value.length > field.validation.maxLength
        ) {
          newErrors[field.name] = `Max ${field.validation.maxLength} chars`;
        }
        if (
          field.validation?.minLength &&
          value &&
          value.length < field.validation.minLength
        ) {
          newErrors[field.name] = `Min ${field.validation.minLength} chars`;
        }
        if (
          field.validation?.pattern &&
          value &&
          !new RegExp(field.validation.pattern).test(value)
        ) {
          newErrors[field.name] = 'Invalid pattern';
        }
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

    // Checkbox input field
    if (field.type === 'checkbox') {
      return (
        <div key={field.name} className='dynamic-form-field'>
          <FormControlLabel
            control={
              <Checkbox
                checked={value as boolean}
                onChange={(e) => handleChange(field.name, e.target.checked)}
                color='primary'
              />
            }
            label={field.label || field.name}
          />
          {error && (
            <FormHelperText error sx={{ mt: 0.5, ml: 0 }}>
              {error}
            </FormHelperText>
          )}
        </div>
      );
    }

    // Select input field
    if (field.type === 'select' && field.options) {
      return (
        <div key={field.name} className='dynamic-form-field'>
          <SelectField
            label={field.label || field.name}
            placeholder={field.placeholder}
            value={String(value)}
            onChange={(newValue) => handleChange(field.name, String(newValue))}
            options={field.options}
            error={!!error}
            helperText={error || undefined}
            className='dynamic-input'
          />
        </div>
      );
    }

    // Special handling for phone input: enforce leading '+' and prevent its deletion
    const isPhoneField = field.name === 'phone';
    const normalizePhoneInput = (raw: string) => {
      const digitsOnly = raw.replace(/[^\d]/g, '');
      return `+${digitsOnly}`;
    };
    const handlePhoneKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
      const input = e.target as HTMLInputElement;
      const selectionStart = input.selectionStart ?? 0;
      const selectionEnd = input.selectionEnd ?? 0;
      // Prevent deleting the leading '+'
      if (
        (e.key === 'Backspace' && selectionStart <= 1 && selectionEnd <= 1) ||
        (e.key === 'Delete' && selectionStart === 0 && selectionEnd === 0)
      ) {
        e.preventDefault();
        return;
      }
      // Prevent typing '+' manually
      if (e.key === '+') {
        e.preventDefault();
      }
    };

    // Text/number/color input fields
    return (
      <div key={field.name} className='dynamic-form-field'>
        <TextInput
          type={field.type}
          placeholder={field.placeholder}
          label={field.label}
          value={
            isPhoneField ?
              (value as string)?.startsWith('+') ?
                value
              : `+${(value as string) || ''}`
            : value
          }
          onChange={(e) =>
            isPhoneField ?
              handleChange(field.name, normalizePhoneInput(e.target.value))
            : handleChange(field.name, e.target.value)
          }
          onKeyDown={isPhoneField ? handlePhoneKeyDown : undefined}
          className='dynamic-input'
          slotProps={{
            htmlInput: {
              maxLength: field.validation?.maxLength || 50,
              inputMode: isPhoneField ? 'tel' : undefined,
              autoComplete: isPhoneField ? 'tel' : undefined,
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
            <div className='modal-body'>
              {fields
                .filter((field) => !field.showOnCreate || isCreate)
                .map(renderField)}
            </div>
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
