import React from 'react';
import TextField, { TextFieldProps } from '@mui/material/TextField';

/**
 * Props for the TextInput component.
 *
 * @param {string} [className] - Optional class name for the field
 */
type CustomInputProps = TextFieldProps & {
  className?: string;
};

/**
 * A reusable text input component with custom styling and Material-UI TextField props.
 */
const TextInput: React.FC<CustomInputProps> = ({ className, sx, ...props }) => {
  return (
    <TextField
      className={className}
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
        ...sx,
      }}
      {...props}
    />
  );
};

export default TextInput;
