import React, { useEffect, useState, useCallback } from 'react';
import Autocomplete from '@mui/material/Autocomplete';
import CircularProgress from '@mui/material/CircularProgress';
import Input from './forms/Input';

export interface SelectFieldProps {
  label: string;
  placeholder?: string;
  value: string;
  onChange: (value: string) => void;
  options?: string[];
  asyncOptions?: (input: string, page?: number) => Promise<{ options: string[] }>;
  loading?: boolean;
  disabled?: boolean;
  className?: string;
  error?: boolean;
  helperText?: string;
}

const debounce = <T extends (...args: any[]) => void>(fn: T, delay: number) => {
  let timeout: number;
  return (...args: Parameters<T>) => {
    clearTimeout(timeout);
    timeout = window.setTimeout(() => fn(...args), delay);
  };
};

const SelectField: React.FC<SelectFieldProps> = ({
  label,
  placeholder,
  value,
  onChange,
  options = [],
  asyncOptions,
  loading: loadingProp = false,
  disabled = false,
  className,
  error,
  helperText,
}) => {
  const [internalOptions, setInternalOptions] = useState<string[]>(options);
  const [loading, setLoading] = useState<boolean>(loadingProp);

  // Debounced async fetch
  const fetchAsyncOptions = useCallback(
    debounce(async (input: string) => {
      if (!asyncOptions) return;
      setLoading(true);
      try {
        const result = await asyncOptions(input, 1);
        setInternalOptions(result.options);
      } finally {
        setLoading(false);
      }
    }, 500),
    [asyncOptions]
  );

  useEffect(() => {
    if (!asyncOptions) {
      setInternalOptions(options);
    }
  }, [options, asyncOptions]);

  return (
    <Autocomplete
      freeSolo={false}
      options={internalOptions}
      loading={loading}
      value={value || ''}
      onInputChange={(_e, inputValue, reason) => {
        if (asyncOptions && reason === 'input') {
          fetchAsyncOptions(inputValue);
        }
      }}
      onChange={(_e, newValue) => onChange(newValue || '')}
      disabled={disabled}
      renderInput={(params) => (
        <Input
          {...params}
          label={label}
          placeholder={placeholder}
          value={value}
          onChange={(e) => onChange(e.target.value)}
          className={className}
          error={error}
          helperText={helperText}
          InputLabelProps={{ shrink: true }}
          InputProps={{
            ...params.InputProps,
            endAdornment: (
              <>
                {loading ? <CircularProgress color='inherit' size={18} /> : null}
                {params.InputProps.endAdornment}
              </>
            ),
          }}
        />
      )}
    />
  );
};

export default SelectField; 