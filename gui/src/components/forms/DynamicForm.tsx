/**
 * DynamicForm Component
 * ---------------------
 * A reusable, dynamic form generator for React applications using TypeScript and Material-UI.
 *
 * This component renders a form based on a configuration array of fields, supporting text, number, and select inputs.
 * It manages its own form state, supports initial values, and provides callbacks for submission and reset.
 *
 * Usage Example:
 * --------------
 * import DynamicForm, { FieldConfig } from './DynamicForm';
 *
 * const fields: FieldConfig[] = [
 *   { name: 'username', type: 'text', placeholder: 'Enter username' },
 *   { name: 'age', type: 'number', placeholder: 'Enter age' },
 *   { name: 'role', type: 'select', placeholder: 'Select role', options: ['Admin', 'User'] },
 * ];
 *
 * <DynamicForm
 *   title="User Form"
 *   fields={fields}
 *   initialValues={{ username: 'John' }}
 *   onSubmit={(data) => console.log(data)}
 * />
 *
 * @module DynamicForm
 */

import React, { useState } from "react";
import Input from "../forms/Input";
import PrimaryButton from "../PrimaryButton";
import SecondaryButton from '../SecondaryButton';
import "./DynamicForm.css";
import {
    Select,
    MenuItem,
    FormControl,
} from "@mui/material";

/**
 * Supported field types for the DynamicForm.
 * Extend this union type to add more input types.
 */
type FieldType = "text" | "number" | "select";

/**
 * Field configuration for each form input.
 * @property name - Unique identifier for the field (used as the key in form state)
 * @property label - Optional label for the field (not currently rendered, but can be used for accessibility)
 * @property placeholder - Placeholder text for the input/select
 * @property type - Type of the field (text, number, select)
 * @property options - For 'select' fields, the list of selectable options
 */
export type FieldConfig = {
    name: string;
    label?: string;
    placeholder: string;
    type: FieldType;
    options?: string[];
};

/**
 * Props for the DynamicForm component.
 * @property title - Title displayed at the top of the form
 * @property fields - Array of field configurations
 * @property initialValues - Optional initial values for the form fields
 * @property onSubmit - Callback invoked with form data on submit
 */
type DynamicFormProps = {
    title: string;
    fields: FieldConfig[];
    initialValues?: Record<string, string>;
    onSubmit: (formData: Record<string, string>) => void;
};

/**
 * DynamicForm React component
 *
 * Renders a form dynamically based on the provided fields configuration.
 * Handles form state, reset, and submission.
 *
 * @param {DynamicFormProps} props - The props for the component
 * @returns {JSX.Element} The rendered form
 */
export default function DynamicForm({
    title,
    fields,
    initialValues = {},
    onSubmit,
}: DynamicFormProps) {
    // Initialize form state with initial values or empty strings
    const [form, setForm] = useState<Record<string, string>>(() => {
        const defaultValues: Record<string, string> = {};
        fields.forEach((field) => {
            defaultValues[field.name] = initialValues[field.name] || "";
        });
        return defaultValues;
    });

    /**
     * Handles changes to any input/select field.
     * @param name - The field name
     * @param value - The new value
     */
    const handleChange = (name: string, value: string) => {
        setForm((prev) => ({ ...prev, [name]: value }));
    };

    /**
     * Resets the form to its initial values.
     */
    const handleReset = () => {
        const resetValues = fields.reduce((acc, field) => {
            acc[field.name] = initialValues[field.name] || "";
            return acc;
        }, {} as Record<string, string>);
        setForm(resetValues);
    };

    /**
     * Handles form submission, prevents default, and calls onSubmit with form data.
     * @param e - The form event
     */
    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onSubmit(form);
    };

    return (
        <div className="dynamic-form-container">
            <form onSubmit={handleSubmit} className="dynamic-form">
                {/* Form Title */}
                <h2 className="dynamic-form-title">{title}</h2>

                {/* Render each field based on its type */}
                {fields.map((field) => {
                    const value = form[field.name];

                    if (field.type === "select") {
                        return (
                            <div key={field.name} className="dynamic-form-field">
                                <FormControl fullWidth>
                                    <Select
                                        value={value}
                                        onChange={(e) => handleChange(field.name, e.target.value as string)}
                                        displayEmpty
                                        className="dynamic-input"
                                    >
                                        {/* Placeholder as disabled option */}
                                        <MenuItem value="" disabled>
                                            {field.placeholder}
                                        </MenuItem>
                                        {/* Render select options */}
                                        {field.options?.map((opt) => (
                                            <MenuItem key={opt} value={opt}>
                                                {opt}
                                            </MenuItem>
                                        ))}
                                    </Select>
                                </FormControl>
                            </div>

                        );
                    }

                    // Render text/number input fields
                    return (
                        <div key={field.name} className="dynamic-form-field">
                            <Input
                                type={field.type}
                                placeholder={field.placeholder}
                                label={undefined}
                                value={value}
                                onChange={(e) => handleChange(field.name, e.target.value)}
                                className="dynamic-input"
                            />
                        </div>
                    );
                })}

                {/* Form action buttons */}
                <div className="dynamic-form-buttons">
                    <SecondaryButton
                        type="button"
                        onClick={handleReset}
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
        </div>
    );
}
