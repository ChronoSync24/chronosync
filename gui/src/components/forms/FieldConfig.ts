export type FieldType = "text" | "number" | "select" | "color";

export type FieldValidation = {
    required?: boolean;
    minLength?: number;
    maxLength?: number;
    pattern?: string;
    custom?: (value: string) => string | null;
};

export type AsyncOptionsResult = {
    options: string[];
    hasMore: boolean;
};

export type FieldConfig = {
    name: string;
    label?: string;
    placeholder: string;
    type: FieldType;
    options?: string[];
    asyncOptions?: (input: string, page: number) => Promise<AsyncOptionsResult>;
    validation?: FieldValidation;
};

// Appointment Type Form Fields
export const appointmentTypeFormFields: FieldConfig[] = [
    { name: "name", label: "Appointment Name", placeholder: "Enter appointment type", type: "text", validation: { required: true, maxLength: 50 } },
    { name: "duration", label: "Duration (minutes)", placeholder: "Enter duration", type: "number", validation: { required: true } },
    { name: "price", label: "Price", placeholder: "Enter price", type: "number" },
    { name: "currency", label: "Currency", placeholder: "Select currency", type: "select", options: ["USD", "EUR", "GBP"], validation: { required: true } },
    { name: "color", label: "Color", placeholder: "Pick a color", type: "color" },
    { //remove after testing
        name: "city",
        label: "City",
        placeholder: "Type to search cities",
        type: "select",
        asyncOptions: async (input, page) => {
            const allCities = [
                "Tuzla", "Zivinice", "Chicago", "LA", "Maribor",
                "Ljubljana", "Amorim", "cr7", "mavs", "Pozz", "Sarajevo", 
                "Srebrenik", "Sladna", "Slatina", "Slavonski Brod", "Slavonski Brod", "Slavonski Brod", 
                "Slavonski Brod", "Slavonski Brod", "Slavonski Brod", "Slavonski Brod", "Slavonski Brod", 
                "Silueta", "Sisa", "Sarma", "Stepenica", "Sikira"
            ];
            const filtered = allCities.filter(city =>
                city.toLowerCase().includes(input.toLowerCase())
            );
            const pageSize = 5;
            const paged = filtered.slice((page - 1) * pageSize, page * pageSize);
            await new Promise(res => setTimeout(res, 500));
            return { options: paged, hasMore: filtered.length > page * pageSize };
        },
        validation: { required: true }
    },
];