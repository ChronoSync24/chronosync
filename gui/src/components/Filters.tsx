import React from 'react';
import {
  Box,
  Collapse,
  Paper,
  Typography,
  TextField,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Button,
  Chip,
  Stack,
  Divider,
} from '@mui/material';
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns';
import { useTheme } from '@mui/material/styles';
import ClearIcon from '@mui/icons-material/Clear';
import PrimaryButton from './PrimaryButton';

export interface FilterField {
  key: string;
  label: string;
  type: 'text' | 'number' | 'select' | 'multiselect' | 'date';
  options?: { value: string | number; label: string }[]; // For select/multiselect
  placeholder?: string;
}

export interface FiltersProps {
  isOpen: boolean;
  fields: FilterField[];
  values: Record<string, any>;
  onFilterChange: (key: string, value: any) => void;
  onClearFilters: () => void;
  onApplyFilters?: () => void;
}

const Filters: React.FC<FiltersProps> = ({
  isOpen,
  fields,
  values,
  onFilterChange,
  onClearFilters,
  onApplyFilters,
}) => {
  const theme = useTheme();

  const sortedFields = React.useMemo(() => {
    const typeOrder = {
      text: 1,
      number: 2,
      select: 3,
      multiselect: 4,
      date: 5,
    };
    return [...fields].sort((a, b) => typeOrder[a.type] - typeOrder[b.type]);
  }, [fields]);

  const renderFilterField = (field: FilterField) => {
    const value = values[field.key];

    const commonFieldStyles = {
      width: '100%',
      minWidth: '240px',
    };

    switch (field.type) {
      case 'text':
        return (
          <TextField
            key={field.key}
            label={field.label}
            placeholder={field.placeholder}
            value={value || ''}
            onChange={(e) => onFilterChange(field.key, e.target.value)}
            size='small'
            sx={commonFieldStyles}
            color='secondary'
          />
        );

      case 'number':
        return (
          <TextField
            key={field.key}
            label={field.label}
            placeholder={field.placeholder}
            type='number'
            value={value || ''}
            onChange={(e) => onFilterChange(field.key, e.target.value)}
            size='small'
            sx={commonFieldStyles}
            color='secondary'
          />
        );

      case 'select':
        return (
          <FormControl key={field.key} size='small' sx={commonFieldStyles}>
            <InputLabel>{field.label}</InputLabel>
            <Select
              value={value || ''}
              label={field.label}
              onChange={(e) => onFilterChange(field.key, e.target.value)}
              color='secondary'>
              <MenuItem value=''>
                <em>All</em>
              </MenuItem>
              {field.options?.map((option) => (
                <MenuItem key={option.value} value={option.value}>
                  {option.label}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        );

      case 'multiselect':
        return (
          <FormControl key={field.key} size='small' sx={commonFieldStyles}>
            <InputLabel>{field.label}</InputLabel>
            <Select
              multiple
              value={Array.isArray(value) ? value : []}
              label={field.label}
              onChange={(e) => onFilterChange(field.key, e.target.value)}
              color='secondary'
              renderValue={(selected) => {
                const selectedArray = selected as string[];
                if (selectedArray.length === 0) return '';

                const maxVisible = 2;
                const visibleItems = selectedArray.slice(0, maxVisible);
                const remainingCount = selectedArray.length - maxVisible;

                return (
                  <Box
                    sx={{
                      display: 'flex',
                      flexWrap: 'nowrap',
                      gap: 0.5,
                      overflow: 'hidden',
                      width: '100%',
                    }}>
                    {visibleItems.map((val) => {
                      const option = field.options?.find(
                        (opt) => opt.value === val
                      );
                      return (
                        <Chip
                          key={val}
                          label={option?.label || val}
                          size='small'
                          sx={{
                            maxWidth: '80px',
                            '& .MuiChip-label': {
                              overflow: 'hidden',
                              textOverflow: 'ellipsis',
                              whiteSpace: 'nowrap',
                            },
                          }}
                          onDelete={() => {
                            const newValue = selectedArray.filter(
                              (s) => s !== val
                            );
                            onFilterChange(field.key, newValue);
                          }}
                          deleteIcon={<ClearIcon />}
                        />
                      );
                    })}
                    {remainingCount > 0 && (
                      <Chip
                        label={`+${remainingCount}`}
                        size='small'
                        variant='outlined'
                        sx={{ minWidth: 'auto' }}
                      />
                    )}
                  </Box>
                );
              }}
              MenuProps={{
                PaperProps: {
                  style: {
                    maxHeight: 200,
                  },
                },
              }}>
              {field.options?.map((option) => (
                <MenuItem key={option.value} value={option.value}>
                  {option.label}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        );

      case 'date':
        return (
          <DatePicker
            key={field.key}
            label={field.label}
            value={value || null}
            onChange={(newValue) => onFilterChange(field.key, newValue)}
            slotProps={{
              textField: {
                size: 'small',
                sx: commonFieldStyles,
                error: false,
                helperText: '',
                color: 'secondary',
              },
            }}
            shouldDisableDate={() => false}
          />
        );

      default:
        return null;
    }
  };

  const hasActiveFilters = Object.values(values).some((value) => {
    if (Array.isArray(value)) {
      return value.length > 0;
    }
    return value !== '' && value !== null && value !== undefined;
  });

  return (
    <LocalizationProvider dateAdapter={AdapterDateFns}>
      <Collapse in={isOpen} timeout='auto' unmountOnExit>
        <Paper
          elevation={0}
          sx={{
            mt: 2,
            py: 2,
            px: 3,
            backgroundColor: theme.palette.background.paper,
            border: `1px solid ${theme.palette.divider}`,
            borderRadius: '12px',
          }}>
          <Box
            sx={{
              mb: 2,
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center',
            }}>
            <Typography variant='h6' gutterBottom sx={{ mb: 0 }}>
              Filters
            </Typography>
            <PrimaryButton
              onClick={onClearFilters}
              disabled={!hasActiveFilters}>
              Clear All
            </PrimaryButton>
          </Box>
          <Divider sx={{ mb: 2 }} />

          <Stack spacing={2}>
            <Box
              sx={{
                display: 'grid',
                gridTemplateColumns: 'repeat(auto-fill, minmax(240px, 300px))',
                gap: 2,
                width: '100%',
                justifyItems: 'start',
              }}>
              {sortedFields.map(renderFilterField)}
            </Box>

            <Box
              sx={{
                display: 'flex',
                gap: 1,
                justifyContent: 'flex-end',
                mt: 2,
              }}>
              {onApplyFilters && (
                <Button
                  variant='contained'
                  onClick={onApplyFilters}
                  size='small'>
                  Apply Filters
                </Button>
              )}
            </Box>

            {hasActiveFilters && (
              <Box sx={{ mt: 2 }}>
                <Typography variant='body2' color='text.secondary' gutterBottom>
                  Active filters:
                </Typography>
                <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1 }}>
                  {Object.entries(values).map(([key, value]) => {
                    if (Array.isArray(value) && value.length === 0) return null;
                    if (!value) return null;

                    const field = sortedFields.find((f) => f.key === key);
                    if (!field) return null;

                    let displayValue = value;
                    if (Array.isArray(value)) {
                      displayValue = value
                        .map((v) => {
                          const option = field.options?.find(
                            (opt) => opt.value === v
                          );
                          return option?.label || v;
                        })
                        .join(', ');
                    } else if (field.type === 'select') {
                      const option = field.options?.find(
                        (opt) => opt.value === value
                      );
                      displayValue = option?.label || value;
                    } else if (field.type === 'date' && value instanceof Date) {
                      displayValue = value.toLocaleDateString();
                    }

                    return (
                      <Chip
                        key={key}
                        label={`${field.label}: ${displayValue}`}
                        size='small'
                        onDelete={() =>
                          onFilterChange(key, Array.isArray(value) ? [] : '')
                        }
                        color='secondary'
                        variant='outlined'
                      />
                    );
                  })}
                </Box>
              </Box>
            )}
          </Stack>
        </Paper>
      </Collapse>
    </LocalizationProvider>
  );
};

export default Filters;
