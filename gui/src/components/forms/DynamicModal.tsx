/**
 * A reusable, dynamic form generator for React applications using TypeScript and Material-UI.
 *
 * This component renders a form based on a configuration array of fields, supporting text, number, and select inputs.
 * It manages its own form state, supports initial values, and provides callbacks for submission and reset.
 *
 * @module DynamicForm
 */

import React, { useState, useCallback, useEffect } from 'react';
import Input from './Input';
import PrimaryButton from '../PrimaryButton';
import SecondaryButton from '../SecondaryButton';
import './DynamicModal.css';
import {
  Select,
  MenuItem,
  FormControl,
  FormHelperText,
  CircularProgress,
  Dialog,
  DialogProps,
} from '@mui/material';
import Autocomplete from '@mui/material/Autocomplete';
import { FieldConfig } from './FieldConfig';

function debounce<T extends (...args: any[]) => void>(fn: T, delay: number) {
  let timeout: number;
  return (...args: Parameters<T>) => {
    clearTimeout(timeout);
    timeout = window.setTimeout(() => fn(...args), delay);
  };
}

type DynamicFormProps = {
  title: string;
  fields: FieldConfig[];
  initialValues?: Record<string, string>;
  onSubmit: (formData: Record<string, string>) => void;
  onCancel?: () => void;
  open: boolean;
  onClose: () => void;
  maxWidth?: DialogProps['maxWidth'];
};

export default function DynamicForm({
  title,
  fields,
  initialValues = {},
  onSubmit,
  onCancel,
  open,
  onClose,
  maxWidth = 'sm',
}: DynamicFormProps) {
  const [form, setForm] = useState<Record<string, string>>(() => {
    const defaultValues: Record<string, string> = {};
    fields.forEach((field: FieldConfig) => {
      defaultValues[field.name] = initialValues[field.name] || '';
    });
    return defaultValues;
  });

  const [errors, setErrors] = useState<Record<string, string>>({});
  const [touched, setTouched] = useState<Record<string, boolean>>({});
  const [asyncOptions, setAsyncOptions] = useState<Record<string, string[]>>(
    {}
  );
  const [loadingAsync, setLoadingAsync] = useState<Record<string, boolean>>({});

  useEffect(() => {
    const defaultValues: Record<string, string> = {};
    fields.forEach((field: FieldConfig) => {
      defaultValues[field.name] = initialValues[field.name] || '';
    });
    setForm(defaultValues);
    setErrors({});
    setTouched({});
  }, [initialValues, fields]);

  // fetch for async dropdowns
  const fetchAsyncOptions = useCallback(
    debounce(async (field: FieldConfig, input: string) => {
      if (!field.asyncOptions) return;
      setLoadingAsync((prev) => ({ ...prev, [field.name]: true }));
      try {
        const result = await field.asyncOptions(input, 1); // page 1 for now
        setAsyncOptions((prev) => ({ ...prev, [field.name]: result.options }));
      } finally {
        setLoadingAsync((prev) => ({ ...prev, [field.name]: false }));
      }
    }, 500),
    []
  );

  const handleChange = (name: string, value: string) => {
    setForm((prev) => ({ ...prev, [name]: value }));
    setErrors((prev) => ({ ...prev, [name]: '' })); // Clear error on change
  };

  const handleBlur = (name: string) => {
    setTouched((prev) => ({ ...prev, [name]: true }));
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
        fields.reduce((acc, field) => {
          acc[field.name] = true;
          return acc;
        }, {} as Record<string, boolean>)
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

    // Async dropdown
    if (field.asyncOptions) {
      return (
        <div key={field.name} className='dynamic-form-field'>
          <Autocomplete
            freeSolo={false}
            options={asyncOptions[field.name] || []}
            loading={loadingAsync[field.name]}
            value={value || ''}
            onInputChange={(_e, inputValue, reason) => {
              if (reason === 'input') {
                fetchAsyncOptions(field, inputValue);
              }
            }}
            onChange={(_e, newValue) =>
              handleChange(field.name, newValue || '')
            }
            renderInput={(params) => (
              <Input
                {...params}
                label={field.label}
                placeholder={field.placeholder}
                value={value}
                onChange={(e) => handleChange(field.name, e.target.value)}
                onBlur={() => handleBlur(field.name)}
                className='dynamic-input'
                error={!!error}
                helperText={error}
                InputLabelProps={{ shrink: true }}
                InputProps={{
                  ...params.InputProps,
                  endAdornment: (
                    <>
                      {loadingAsync[field.name] ?
                        <CircularProgress color='inherit' size={18} />
                      : null}
                      {params.InputProps.endAdornment}
                    </>
                  ),
                }}
              />
            )}
          />
        </div>
      );
    }

    // Static dropdown
    if (field.type === 'select' && field.options) {
      return (
        <div key={field.name} className='dynamic-form-field'>
          <FormControl fullWidth error={!!error}>
            <Select
              value={value}
              onChange={(e) =>
                handleChange(field.name, e.target.value as string)
              }
              displayEmpty
              className='dynamic-input'
              onBlur={() => handleBlur(field.name)}>
              <MenuItem value='' disabled>
                {field.placeholder}
              </MenuItem>
              {field.options.map((opt: string) => (
                <MenuItem key={opt} value={opt}>
                  {opt}
                </MenuItem>
              ))}
            </Select>
            {error && <FormHelperText>{error}</FormHelperText>}
          </FormControl>
        </div>
      );
    }

    // Text/number input fields
    return (
      <div key={field.name} className='dynamic-form-field'>
        <Input
          type={field.type}
          placeholder={field.placeholder}
          label={field.label}
          value={value}
          onChange={(e) => handleChange(field.name, e.target.value)}
          onBlur={() => handleBlur(field.name)}
          className='dynamic-input'
          inputProps={{ maxLength: field.validation?.maxLength || 50 }}
          error={!!error}
          helperText={error}
        />
      </div>
    );
  };

  // Main render
  const formContent = (
    <form onSubmit={handleSubmit} className='dynamic-form' noValidate>
      <div className='modal-header'>
        <h2 className='dynamic-form-title'>{title}</h2>
      </div>
      <div className='modal-body'>
        {/* Render each field based on its type */}
        {fields.map(renderField)}
      </div>
      <div className='modal-footer dynamic-form-buttons'>
        <SecondaryButton
          type='button'
          onClick={handleCancel}
          className='dynamic-button-cancel'>
          Cancel
        </SecondaryButton>
        <PrimaryButton type='submit' className='dynamic-button-submit'>
          Create
        </PrimaryButton>
      </div>
    </form>
  );

  return (
    <Dialog open={open} onClose={onClose} maxWidth={maxWidth} fullWidth>
      {formContent}
    </Dialog>
  );
}
