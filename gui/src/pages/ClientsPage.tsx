import React from 'react';
import { Box, Typography, Button, IconButton, Theme, useTheme } from '@mui/material';
import ReactTable, { TableColumn } from '../components/ReactTable';
import Filters, { FilterField } from '../components/Filters';
import FilterAltOutlinedIcon from '@mui/icons-material/FilterAltOutlined';
import AddIcon from '@mui/icons-material/Add';
import { Client } from '../models/Client';

const ClientsPage: React.FC = () => {
  const theme: Theme = useTheme();

  // Sample data - replace with real data from your API
  const [clients, setClients] = React.useState<Client[]>([
    { 
      id: 1, 
      firstName: 'John',
      lastName: 'Doe',
      email: 'john.doe@example.com',
      phone: '+1 (555) 123-4567'
    },
    { 
      id: 2, 
      firstName: 'Jane',
      lastName: 'Smith',
      email: 'jane.smith@example.com',
      phone: '+1 (555) 234-5678'
    },
    { 
      id: 3, 
      firstName: 'Bob',
      lastName: 'Johnson',
      email: 'bob.johnson@example.com',
      phone: '+1 (555) 345-6789'
    },
    { 
      id: 4, 
      firstName: 'Alice',
      lastName: 'Brown',
      email: 'alice.brown@example.com',
      phone: '+1 (555) 456-7890'
    },
    { 
      id: 5, 
      firstName: 'Charlie',
      lastName: 'Wilson',
      email: 'charlie.wilson@example.com',
      phone: '+1 (555) 567-8901'
    },
    { 
      id: 6, 
      firstName: 'Diana',
      lastName: 'Davis',
      email: 'diana.davis@example.com',
      phone: '+1 (555) 678-9012'
    },
    { 
      id: 7, 
      firstName: 'Edward',
      lastName: 'Miller',
      email: 'edward.miller@example.com',
      phone: '+1 (555) 789-0123'
    },
    { 
      id: 8, 
      firstName: 'Fiona',
      lastName: 'Garcia',
      email: 'fiona.garcia@example.com',
      phone: '+1 (555) 890-1234'
    },
  ]);

  // Filters state
  const [isFiltersOpen, setIsFiltersOpen] = React.useState(false);
  const [filterValues, setFilterValues] = React.useState<Record<string, any>>({});
  const [filteredClients, setFilteredClients] = React.useState(clients);

  // Define filter fields for this page
  const filterFields: FilterField[] = [
    {
      key: 'firstName',
      label: 'First Name',
      type: 'text',
      placeholder: 'Search by first name...',
    },
    {
      key: 'lastName',
      label: 'Last Name',
      type: 'text',
      placeholder: 'Search by last name...',
    },
    {
      key: 'email',
      label: 'Email',
      type: 'text',
      placeholder: 'Search by email...',
    },
    {
      key: 'phone',
      label: 'Phone',
      type: 'text',
      placeholder: 'Search by phone...',
    },
  ];

  // Filter logic
  const applyFilters = React.useCallback(() => {
    let filtered = [...clients];

    Object.entries(filterValues).forEach(([key, value]) => {
      if (!value || (Array.isArray(value) && value.length === 0)) return;

      filtered = filtered.filter((client) => {
        const clientValue = client[key as keyof typeof client];
        
        if (Array.isArray(value)) {
          return value.includes(clientValue);
        }
        
        if (typeof clientValue === 'string' && typeof value === 'string') {
          return clientValue.toLowerCase().includes(value.toLowerCase());
        }
        
        return clientValue === value;
      });
    });

    setFilteredClients(filtered);
  }, [filterValues, clients]);

  // Apply filters whenever filter values change
  React.useEffect(() => {
    applyFilters();
  }, [applyFilters]);

  const handleDelete = (id: number) => {
    setClients(clients.filter(client => client.id !== id));
  };

  const handleEdit = (id: number) => {
    console.log('Edit client:', id);
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

  const formatPhone = (phone: string) => {
    // Simple phone formatting - you can enhance this based on your needs
    return phone;
  };

  const columns: TableColumn[] = [
    {
      key: 'id',
      label: 'ID',
      align: 'left',
    },
    {
      key: 'firstName',
      label: 'First Name',
      align: 'left',
    },
    {
      key: 'lastName',
      label: 'Last Name',
      align: 'left',
    },
    {
      key: 'email',
      label: 'Email',
      align: 'left',
    },
    {
      key: 'phone',
      label: 'Phone',
      align: 'left',
      render: (value) => formatPhone(value),
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
          Clients
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
          data={filteredClients}
        />
      </Box>
    </Box>
  );
};

export default ClientsPage;
