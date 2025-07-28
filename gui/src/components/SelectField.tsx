import React, { useEffect, useState, useCallback } from 'react';
import Autocomplete from '@mui/material/Autocomplete';
import CircularProgress from '@mui/material/CircularProgress';
import TextField from '@mui/material/TextField';

export interface OptionType {
  value: string | number;
  label: string;
}

export interface SelectFieldProps {
  label: string;
  placeholder?: string;
  value: string | number;
  onChange: (value: string | number) => void;
  options?: OptionType[];
  loadOptions?: (searchTerm: string) => Promise<OptionType[]>;
  loading?: boolean;
  disabled?: boolean;
  className?: string;
  error?: boolean;
  helperText?: string;
  size?: 'small' | 'medium';
  sx?: object;
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
  loadOptions,
  loading: loadingProp = false,
  disabled = false,
  className,
  error,
  helperText,
  size = 'medium',
  sx,
}) => {
  const [internalOptions, setInternalOptions] = useState<OptionType[]>(options);
  const [loading, setLoading] = useState<boolean>(loadingProp);

  const fetchAsyncOptions = useCallback(
    debounce(async (input: string) => {
      if (!loadOptions) return;
      setLoading(true);
      try {
        const result = await loadOptions(input);
        setInternalOptions(result);
      } catch (error) {
        console.error('Error loading options:', error);
        setInternalOptions([]);
      } finally {
        setLoading(false);
      }
    }, 500),
    [loadOptions]
  );

  useEffect(() => {
    if (!loadOptions) {
      setInternalOptions(options);
    }
  }, [options, loadOptions]);

  // Load initial options for async fields
  useEffect(() => {
    if (loadOptions) {
      fetchAsyncOptions('');
    }
  }, [loadOptions, fetchAsyncOptions]);

  const selectedOption =
    internalOptions.find((option) => option.value === value) || null;

  return (
    <Autocomplete
      size={size}
      sx={sx}
      options={internalOptions}
      getOptionLabel={(option) => option.label}
      value={selectedOption}
      loading={loading}
      onInputChange={(_e, inputValue, reason) => {
        if (loadOptions && reason === 'input') {
          fetchAsyncOptions(inputValue);
        }
      }}
      onChange={(_e, newValue) => {
        onChange(newValue?.value || '');
      }}
      disabled={disabled}
      renderInput={(params) => (
        <TextField
          {...params}
          label={label}
          placeholder={placeholder}
          className={className}
          error={error}
          helperText={helperText}
          variant='outlined'
          fullWidth
          size='small'
          sx={{
            '& .MuiOutlinedInput-root': {
              borderRadius: '8px',
              '& .MuiOutlinedInput-notchedOutline': {
                borderRadius: '8px',
              },
            },
          }}
          InputProps={{
            ...params.InputProps,
            endAdornment: (
              <>
                {loading ?
                  <CircularProgress color='inherit' size={18} />
                : null}
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
