/**
 * Input Component
 * --------------
 * A thin wrapper around Material-UI's TextField, allowing for custom styling and prop extension.
 *
 * This component is used as a building block for form fields, providing a consistent interface and style.
 *
 * Usage Example:
 * --------------
 * import Input from './Input';
 *
 * <Input
 *   type="text"
 *   placeholder="Enter your name"
 *   value={value}
 *   onChange={handleChange}
 *   className="my-input-class"
 * />
 *
 * @module Input
 */

import React from 'react';
import TextField, { TextFieldProps } from '@mui/material/TextField';

/**
 * Props for the Input component.
 * Extends Material-UI's TextFieldProps and adds an optional className for custom styling.
 */
type CustomInputProps = TextFieldProps & {
    className?: string;
};

/**
 * Input React component
 *
 * Renders a Material-UI TextField with forwarded props and custom className.
 *
 * @param {CustomInputProps} props - The props for the component
 * @returns {JSX.Element} The rendered input field
 */
const Input: React.FC<CustomInputProps> = ({ className, ...props }) => {
    return (
        <TextField
            className={className}
            variant="outlined"
            fullWidth
            size="small"
            InputProps={{
                ...props.InputProps,
                sx: {
                    borderRadius: '8px',
                    '& .MuiOutlinedInput-notchedOutline': {
                        borderRadius: '8px',
                    },
                    ...props.InputProps?.sx,
                },
            }}
            {...props}
        />
    );
};

export default Input;
