/**
 * A reusable, dynamic form generator for React applications using TypeScript and Material-UI.
 *
 * This component renders a form based on a configuration array of fields, supporting text, number, and select inputs.
 * It manages its own form state, supports initial values, and provides callbacks for submission and reset.
 *
 * @module DynamicForm
 */

import React, { useState, useCallback } from "react";
import Input from "../forms/Input";
import PrimaryButton from "../PrimaryButton";
import SecondaryButton from '../SecondaryButton';
import "./DynamicForm.css";
import {
    Select,
    MenuItem,
    FormControl,
    FormHelperText,
    CircularProgress,
} from "@mui/material";
import Autocomplete from '@mui/material/Autocomplete';
import { FieldConfig } from "../forms/FieldConfig";

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
    noContainer?: boolean;
    onCancel?: () => void;
};

export default function DynamicForm({
    title,
    fields,
    initialValues = {},
    onSubmit,
    noContainer = false,
    onCancel,
}: DynamicFormProps) {
    const [form, setForm] = useState<Record<string, string>>(() => {
        const defaultValues: Record<string, string> = {};
        fields.forEach((field: FieldConfig) => {
            defaultValues[field.name] = initialValues[field.name] || "";
        });
        return defaultValues;
    });

    const [errors, setErrors] = useState<Record<string, string>>({});
    const [touched, setTouched] = useState<Record<string, boolean>>({});
    const [asyncOptions, setAsyncOptions] = useState<Record<string, string[]>>({});
    const [loadingAsync, setLoadingAsync] = useState<Record<string, boolean>>({});

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

    /**
     * Handles changes to any input/select field.
     * @param name - The field name
     * @param value - The new value
     */
    const handleChange = (name: string, value: string) => {
        setForm((prev) => ({ ...prev, [name]: value }));
        setErrors((prev) => ({ ...prev, [name]: "" })); // Clear error on change
    };

    /**
     * Handles blur event to mark field as touched
     */
    const handleBlur = (name: string) => {
        setTouched((prev) => ({ ...prev, [name]: true }));
    };

    /**
     * Resets the form to its initial values.
     */
    const handleReset = () => {
        const resetValues = fields.reduce((acc: Record<string, string>, field: FieldConfig) => {
            acc[field.name] = initialValues[field.name] || "";
            return acc;
        }, {} as Record<string, string>);
        setForm(resetValues);
        setErrors({});
        setTouched({});
    };

    /**
     * Validates the form fields using the validation property in FieldConfig.
     * @returns {boolean} True if valid, false otherwise
     */
    const validate = () => {
        const newErrors: Record<string, string> = {};
        fields.forEach((field: FieldConfig) => {
            const value = form[field.name] || "";
            const v = field.validation;
            if (v?.required && !value.trim()) {
                newErrors[field.name] = "This field is required.";
            } else if (v?.minLength && value.length < v.minLength) {
                newErrors[field.name] = `Minimum ${v.minLength} characters.`;
            } else if (v?.maxLength && value.length > v.maxLength) {
                newErrors[field.name] = `Maximum ${v.maxLength} characters allowed.`;
            } else if (v?.pattern && !new RegExp(v.pattern).test(value)) {
                newErrors[field.name] = "Invalid format.";
            } else if (v?.custom) {
                const customError = v.custom(value);
                if (customError) newErrors[field.name] = customError;
            }
        });
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    /**
     * Handles form submission, prevents default, and calls onSubmit with form data.
     * @param e - The form event
     */
    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        // Mark all as touched for error display
        const allTouched: Record<string, boolean> = {};
        fields.forEach((f) => (allTouched[f.name] = true));
        setTouched(allTouched);
        if (validate()) {
            onSubmit(form);
        }
    };

    const handleCancel = () => {
        if (onCancel) {
            onCancel();
        } else {
            handleReset();
        }
    };

    // Render a field based on its type and config
    const renderField = (field: FieldConfig) => {
        const value = form[field.name];
        const error = touched[field.name] && errors[field.name];

        // Async dropdown
        if (field.asyncOptions) {
            return (
                <div key={field.name} className="dynamic-form-field">
                    <Autocomplete
                        freeSolo={false}
                        options={asyncOptions[field.name] || []}
                        loading={loadingAsync[field.name]}
                        value={value || ""}
                        onInputChange={(_e, inputValue, reason) => {
                            if (reason === 'input') {
                                fetchAsyncOptions(field, inputValue);
                            }
                        }}
                        onChange={(_e, newValue) => handleChange(field.name, newValue || "")}
                        renderInput={(params) => (
                            <Input
                                {...params}
                                label={field.label}
                                placeholder={field.placeholder}
                                value={value}
                                onChange={(e) => handleChange(field.name, e.target.value)}
                                onBlur={() => handleBlur(field.name)}
                                className="dynamic-input"
                                error={!!error}
                                helperText={error}
                                InputLabelProps={{ shrink: true }}
                                InputProps={{
                                    ...params.InputProps,
                                    endAdornment: (
                                        <>
                                            {loadingAsync[field.name] ? <CircularProgress color="inherit" size={18} /> : null}
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
        if (field.type === "select" && field.options) {
            return (
                <div key={field.name} className="dynamic-form-field">
                    <FormControl fullWidth error={!!error}>
                        <Select
                            value={value}
                            onChange={(e) => handleChange(field.name, e.target.value as string)}
                            displayEmpty
                            className="dynamic-input"
                            onBlur={() => handleBlur(field.name)}
                        >
                            <MenuItem value="" disabled>
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
            <div key={field.name} className="dynamic-form-field">
                <Input
                    type={field.type}
                    placeholder={field.placeholder}
                    label={field.label}
                    value={value}
                    onChange={(e) => handleChange(field.name, e.target.value)}
                    onBlur={() => handleBlur(field.name)}
                    className="dynamic-input"
                    inputProps={{ maxLength: field.validation?.maxLength || 50 }}
                    error={!!error}
                    helperText={error}
                />
            </div>
        );
    };

    // Main render
    const formContent = (
        <form onSubmit={handleSubmit} className="dynamic-form" noValidate>
            <h2 className="dynamic-form-title">{title}</h2>
            {/* Render each field based on its type */}
            {fields.map(renderField)}
            <div className="dynamic-form-buttons">
                <SecondaryButton
                    type="button"
                    onClick={handleCancel}
                    className="dynamic-button-cancel"
                >
                    Cancel
                </SecondaryButton>
                <PrimaryButton
                    type="submit"
                    className="dynamic-button-submit"
                >
                    Create
                </PrimaryButton>
            </div>
        </form>
    );

    if (noContainer) {
        return formContent;
    }
    return <div className="dynamic-form-container">{formContent}</div>;
}
