import React from 'react';
import { Box, Typography, Button } from '@mui/material';
import ReactTable, { TableColumn } from '../components/ReactTable';

const UsersPage: React.FC = () => {
  // Sample data - replace with real data from your API
  const [users, setUsers] = React.useState([
    { id: 1, name: 'John Doe', email: 'john@example.com' },
    { id: 2, name: 'Jane Smith', email: 'jane@example.com' },
  ]);

  const handleDelete = (id: number) => {
    setUsers(users.filter(user => user.id !== id));
  };

  const handleEdit = (id: number) => {
    console.log('Edit user:', id);
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
      key: 'email',
      label: 'Email',
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
            color="primary" 
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
      <Typography variant="h4" gutterBottom>
        User Management
      </Typography>
      
      <ReactTable 
        columns={columns}
        data={users}
      />
    </Box>
  );
};

export default UsersPage;