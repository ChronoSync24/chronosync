import React from 'react';
import { Box, Typography, Button, IconButton, Theme, useTheme, Chip } from '@mui/material';
import ReactTable, { TableColumn } from '../components/ReactTable';
import Filters, { FilterField } from '../components/Filters';
import FilterAltOutlinedIcon from '@mui/icons-material/FilterAltOutlined';
import AddIcon from '@mui/icons-material/Add';
import { AppointmentType } from '../models/appointmentType/AppointmentType';
import { Currency } from '../models/appointmentType/Currency';

const AppointmentTypePage: React.FC = () => {
  const theme: Theme = useTheme();

  // Sample data - replace with real data from your API
  const [appointmentTypes, setAppointmentTypes] = React.useState<AppointmentType[]>([
    {
      id: 1,
      name: 'Initial Consultation',
      durationMinutes: 60,
      price: 150.00,
      colorCode: '#4CAF50',
      currency: Currency.USD
    },
    {
      id: 2,
      name: 'Follow-up Session',
      durationMinutes: 30,
      price: 75.00,
      colorCode: '#2196F3',
      currency: Currency.USD
    },
    {
      id: 3,
      name: 'Emergency Visit',
      durationMinutes: 45,
      price: 200.00,
      colorCode: '#F44336',
      currency: Currency.USD
    },
    {
      id: 4,
      name: 'Annual Checkup',
      durationMinutes: 90,
      price: 250.00,
      colorCode: '#9C27B0',
      currency: Currency.USD
    },
    {
      id: 5,
      name: 'Quick Consultation',
      durationMinutes: 15,
      price: 50.00,
      colorCode: '#FF9800',
      currency: Currency.USD
    },
    {
      id: 6,
      name: 'Specialist Review',
      durationMinutes: 120,
      price: 300.00,
      colorCode: '#607D8B',
      currency: Currency.USD
    },
  ]);

  // Filters state
  const [isFiltersOpen, setIsFiltersOpen] = React.useState(false);
  const [filterValues, setFilterValues] = React.useState<Record<string, any>>({});
  const [filteredAppointmentTypes, setFilteredAppointmentTypes] = React.useState(appointmentTypes);

  // Define filter fields for this page
  const filterFields: FilterField[] = [
    {
      key: 'name',
      label: 'Name',
      type: 'text',
      placeholder: 'Search by name...',
    },
    {
      key: 'durationMinutes',
      label: 'Duration (minutes)',
      type: 'number',
      placeholder: 'Enter duration...',
    },
    {
      key: 'price',
      label: 'Price',
      type: 'number',
      placeholder: 'Enter price...',
    },
    {
      key: 'currency',
      label: 'Currency',
      type: 'select',
      options: [
        { value: Currency.USD, label: 'USD' },
        { value: Currency.EUR, label: 'EUR' },
        { value: Currency.CHF, label: 'CHF' },
        { value: Currency.GBP, label: 'GBP' },
        { value: Currency.BAM, label: 'BAM' },
      ],
    },
    {
      key: 'colorCode',
      label: 'Color',
      type: 'text',
      placeholder: 'Search by color...',
    },
  ];

  // Filter logic
  const applyFilters = React.useCallback(() => {
    let filtered = [...appointmentTypes];

    Object.entries(filterValues).forEach(([key, value]) => {
      if (!value || (Array.isArray(value) && value.length === 0)) return;

      filtered = filtered.filter((appointmentType) => {
        const appointmentTypeValue = appointmentType[key as keyof typeof appointmentType];

        if (Array.isArray(value)) {
          return value.includes(appointmentTypeValue);
        }

        // Handle number filtering (exact match for now, could be enhanced with ranges)
        if (typeof value === 'number' || (!isNaN(Number(value)) && value !== '')) {
          return Number(appointmentTypeValue) === Number(value);
        }

        if (typeof appointmentTypeValue === 'string' && typeof value === 'string') {
          return appointmentTypeValue.toLowerCase().includes(value.toLowerCase());
        }

        return appointmentTypeValue === value;
      });
    });

    setFilteredAppointmentTypes(filtered);
  }, [filterValues, appointmentTypes]);

  // Apply filters whenever filter values change
  React.useEffect(() => {
    applyFilters();
  }, [applyFilters]);

  const handleDelete = (id: number) => {
    setAppointmentTypes(appointmentTypes.filter(appointmentType => appointmentType.id !== id));
  };

  const handleEdit = (id: number) => {
    console.log('Edit appointment type:', id);
  };

  const handleFilterChange = (key: string, value: any) => {
    setFilterValues(prev => ({ ...prev, [key]: value }));
  };

  const handleClearFilters = () => {
    setFilterValues({});
  };

  const toggleFilters = () => {
    setIsFiltersOpen(!isFiltersOpen);
  };

  const formatDuration = (minutes: number) => {
    const hours = Math.floor(minutes / 60);
    const remainingMinutes = minutes % 60;

    if (hours > 0 && remainingMinutes > 0) {
      return `${hours}h ${remainingMinutes}m`;
    } else if (hours > 0) {
      return `${hours}h`;
    } else {
      return `${remainingMinutes}m`;
    }
  };

  const formatPrice = (price: number, currency: Currency) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: currency,
    }).format(price);
  };

  const columns: TableColumn[] = [
    {
      key: 'id',
      label: 'ID',
      align: 'left',
    },
    {
      key: 'name',
      label: 'Name',
      align: 'left',
    },
    {
      key: 'durationMinutes',
      label: 'Duration',
      align: 'left',
      render: (value) => formatDuration(value),
    },
    {
      key: 'price',
      label: 'Price',
      align: 'left',
      render: (value, row) => formatPrice(value, row.currency),
    },
    {
      key: 'currency',
      label: 'Currency',
      align: 'left',
    },
    {
      key: 'colorCode',
      label: 'Color',
      align: 'left',
      render: (value) => (
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
          <Box
            sx={{
              width: 20,
              height: 20,
              borderRadius: '50%',
              backgroundColor: value,
              border: '1px solid #ddd',
            }}
          />
          <Typography variant="body2">{value}</Typography>
        </Box>
      ),
    },
    {
      key: 'actions',
      label: '',
      align: 'right',
      render: (_, row) => (
        <Box sx={{ display: 'flex', gap: 1, justifyContent: 'right' }}>
          <Button
            variant="outlined"
            color="secondary"
            size="small"
            onClick={() => handleEdit(row.id)}
          >
            Edit
          </Button>
          <Button
            variant="outlined"
            color="error"
            size="small"
            onClick={() => handleDelete(row.id)}
          >
            Delete
          </Button>
        </Box>
      ),
    },
  ];

  return (
    <Box sx={{ p: 3 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Typography variant="h4" gutterBottom>
          Appointment Types
        </Typography>
        <Box>
          <IconButton
            onClick={toggleFilters}
            sx={{
              borderRadius: '8px',
              marginRight: '8px',
              color: theme.palette.secondary.main,
              border: `1px solid ${isFiltersOpen ? theme.palette.secondary.main : 'transparent'}`,
              '&:hover': {
                backgroundColor: theme.palette.background.paper,
                border: `1px solid ${theme.palette.secondary.main}`,
              }
            }}>
            <FilterAltOutlinedIcon fontSize='medium' />
          </IconButton>
          <IconButton
            sx={{
              borderRadius: '8px',
              color: theme.palette.secondary.main,
              border: `1px solid transparent`,
              '&:hover': {
                backgroundColor: theme.palette.background.paper,
                border: `1px solid ${theme.palette.secondary.main}`,
              }
            }}>
            <AddIcon fontSize='medium' />
          </IconButton>
        </Box>
      </Box>

      {/* Filters */}
      <Filters
        isOpen={isFiltersOpen}
        fields={filterFields}
        values={filterValues}
        onFilterChange={handleFilterChange}
        onClearFilters={handleClearFilters}
      />

      <Box sx={{ mt: 2 }}>
        <ReactTable
          columns={columns}
          data={filteredAppointmentTypes}
        />
      </Box>
    </Box>
  );
};

export default AppointmentTypePage;
