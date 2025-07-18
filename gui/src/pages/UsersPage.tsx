import React from 'react';
import { Box, Typography, Button, IconButton, Theme, useTheme } from '@mui/material';
import ReactTable, { TableColumn } from '../components/ReactTable';
import Filters, { FilterField } from '../components/Filters';
import FilterAltOutlinedIcon from '@mui/icons-material/FilterAltOutlined';
import AddIcon from '@mui/icons-material/Add';
import { OverlayContext } from '../App';
import DynamicForm from '../components/forms/DynamicForm';
import { userFormFields } from '../components/forms/FieldConfig';

const UsersPage: React.FC = () => {
  const theme: Theme = useTheme();
  const overlay = React.useContext(OverlayContext);

  // Sample data - replace with real data from your API
  const [users, setUsers] = React.useState([
    { 
      id: 1, 
      name: 'John Doe', 
      email: 'john@example.com', 
      role: 'admin', 
      status: 'active',
      department: 'IT',
      age: 32,
      joinDate: '2022-01-15',
      salary: 75000,
      location: 'New York'
    },
    { 
      id: 2, 
      name: 'Jane Smith', 
      email: 'jane@example.com', 
      role: 'user', 
      status: 'inactive',
      department: 'HR',
      age: 28,
      joinDate: '2021-06-20',
      salary: 55000,
      location: 'Los Angeles'
    },
    { 
      id: 3, 
      name: 'Bob Johnson', 
      email: 'bob@example.com', 
      role: 'moderator', 
      status: 'active',
      department: 'Marketing',
      age: 45,
      joinDate: '2020-03-10',
      salary: 65000,
      location: 'Chicago'
    },
    { 
      id: 4, 
      name: 'Alice Brown', 
      email: 'alice@example.com', 
      role: 'user', 
      status: 'active',
      department: 'Finance',
      age: 31,
      joinDate: '2023-02-01',
      salary: 70000,
      location: 'Miami'
    },
    { 
      id: 5, 
      name: 'Charlie Wilson', 
      email: 'charlie@example.com', 
      role: 'admin', 
      status: 'active',
      department: 'IT',
      age: 38,
      joinDate: '2019-11-12',
      salary: 85000,
      location: 'Seattle'
    },
    { 
      id: 6, 
      name: 'Diana Davis', 
      email: 'diana@example.com', 
      role: 'user', 
      status: 'pending',
      department: 'Sales',
      age: 26,
      joinDate: '2023-08-15',
      salary: 50000,
      location: 'Boston'
    },
  ]);

  // Filters state
  const [isFiltersOpen, setIsFiltersOpen] = React.useState(false);
  const [filterValues, setFilterValues] = React.useState<Record<string, any>>({});
  const [filteredUsers, setFilteredUsers] = React.useState(users);
  // Update editingUser type to match user object
  const [editingUser, setEditingUser] = React.useState<{
    id: number;
    name: string;
    email: string;
    role: string;
    status: string;
    department: string;
    age: number;
    joinDate: string;
    salary: number;
    location: string;
  } | null>(null);

  // Define filter fields for this page - more filters for testing
  const filterFields: FilterField[] = [
    {
      key: 'name',
      label: 'Name',
      type: 'text',
      placeholder: 'Search by name...',
    },
    {
      key: 'email',
      label: 'Email',
      type: 'text',
      placeholder: 'Search by email...',
    },
    {
      key: 'age',
      label: 'Age',
      type: 'number',
      placeholder: 'Enter age...',
    },
    {
      key: 'salary',
      label: 'Salary',
      type: 'number',
      placeholder: 'Enter salary...',
    },
    {
      key: 'role',
      label: 'Role',
      type: 'select',
      options: [
        { value: 'admin', label: 'Admin' },
        { value: 'user', label: 'User' },
        { value: 'moderator', label: 'Moderator' },
      ],
    },
    {
      key: 'department',
      label: 'Department',
      type: 'select',
      options: [
        { value: 'IT', label: 'IT' },
        { value: 'HR', label: 'HR' },
        { value: 'Marketing', label: 'Marketing' },
        { value: 'Finance', label: 'Finance' },
        { value: 'Sales', label: 'Sales' },
      ],
    },
    {
      key: 'location',
      label: 'Location',
      type: 'select',
      options: [
        { value: 'New York', label: 'New York' },
        { value: 'Los Angeles', label: 'Los Angeles' },
        { value: 'Chicago', label: 'Chicago' },
        { value: 'Miami', label: 'Miami' },
        { value: 'Seattle', label: 'Seattle' },
        { value: 'Boston', label: 'Boston' },
      ],
    },
    {
      key: 'status',
      label: 'Status',
      type: 'multiselect',
      options: [
        { value: 'active', label: 'Active' },
        { value: 'inactive', label: 'Inactive' },
        { value: 'pending', label: 'Pending' },
        { value: 'suspended', label: 'Suspended' },
      ],
    },
    {
      key: 'joinDate',
      label: 'Join Date',
      type: 'date',
    },
  ];

  // Filter logic
  const applyFilters = React.useCallback(() => {
    let filtered = [...users];

    Object.entries(filterValues).forEach(([key, value]) => {
      if (!value || (Array.isArray(value) && value.length === 0)) return;

      filtered = filtered.filter((user) => {
        const userValue = user[key as keyof typeof user];
        
        if (Array.isArray(value)) {
          return value.includes(userValue);
        }
        
        // Handle date filtering
        if (key === 'joinDate' && value instanceof Date) {
          const userDate = new Date(userValue as string);
          const filterDate = new Date(value);
          // Compare dates (same day)
          return userDate.toDateString() === filterDate.toDateString();
        }
        
        // Handle number filtering (exact match for now, could be enhanced with ranges)
        if (typeof value === 'number' || (!isNaN(Number(value)) && value !== '')) {
          return Number(userValue) === Number(value);
        }
        
        if (typeof userValue === 'string' && typeof value === 'string') {
          return userValue.toLowerCase().includes(value.toLowerCase());
        }
        
        return userValue === value;
      });
    });

    setFilteredUsers(filtered);
  }, [filterValues, users]);

  // Apply filters whenever filter values change
  React.useEffect(() => {
    applyFilters();
  }, [applyFilters]);

  const handleDelete = (id: number) => {
    setUsers(users.filter(user => user.id !== id));
  };

  const handleEdit = (id: number) => {
    const user = users.find(u => u.id === id);
    if (user) {
      setEditingUser(user);
      overlay.open(
        <DynamicForm
          title="Edit User"
          fields={userFormFields}
          initialValues={Object.fromEntries(Object.entries(user).map(([k, v]) => [k, String(v)]))}
          onSubmit={handleEditFormSubmit}
          onCancel={() => {
            setEditingUser(null);
            overlay.close();
          }}
          noContainer={true}
        />
      );
    }
  };

  // Update handleEditFormSubmit to use the correct type
  const handleEditFormSubmit = (formData: any) => {
    setUsers((prev: any[]) =>
      prev.map(u =>
        u.id === editingUser?.id
          ? {
              ...u,
              name: formData.name,
              email: formData.email,
              role: formData.role,
              status: formData.status,
              department: formData.department,
              location: formData.location,
              age: u.age,
              joinDate: u.joinDate,
              salary: u.salary,
              id: u.id
            }
          : u
      )
    );
    setEditingUser(null);
    overlay.close();
  };

  const handleFilterChange = (key: string, value: any) => {
    setFilterValues(prev => ({ ...prev, [key]: value }));
  };

  const handleClearFilters = () => {
    setFilterValues({});
  };

  const handleFormSubmit = (formData: Record<string, string>) => {
    setUsers(prev => [
      ...prev,
      {
        id: prev.length ? Math.max(...prev.map(u => u.id)) + 1 : 1,
        name: formData.name,
        email: formData.email,
        role: formData.role,
        status: formData.status,
        department: formData.department,
        location: formData.location,
        age: 0,
        joinDate: new Date().toISOString().split('T')[0],
        salary: 0,
      }
    ]);
    overlay.close();
  };

  const columns: TableColumn[] = [
    {
      key: 'id',
      label: 'ID',
      align: 'left',
      width: '80px',
    },
    {
      key: 'name',
      label: 'Name',
      align: 'left',
    },
    {
      key: 'email',
      label: 'Email',
      align: 'left',
    },
    {
      key: 'department',
      label: 'Department',
      align: 'left',
    },
    {
      key: 'role',
      label: 'Role',
      align: 'left',
    },
    {
      key: 'status',
      label: 'Status',
      align: 'left',
    },
    {
      key: 'location',
      label: 'Location',
      align: 'left',
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

  // Restore the toggleFilters function if missing
  const toggleFilters = () => {
    setIsFiltersOpen(!isFiltersOpen);
  };

  return (
    <Box sx={{ p: 3 }}>
      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Typography variant="h4" gutterBottom>
          User Management
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
          }}
          onClick={() => overlay.open(
            <DynamicForm
              title="Create User"
              fields={userFormFields}
              onSubmit={handleFormSubmit}
              onCancel={overlay.close}
              noContainer={true}
            />
          )}
          >
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
          data={filteredUsers}
        />
      </Box>
    </Box>
  );
};

export default UsersPage;