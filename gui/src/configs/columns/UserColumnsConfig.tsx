import React from 'react';
import { Box, Button } from '@mui/material';
import { TableColumn } from '../../components/ReactTable';
import { UserRole } from '../../models/user/UserRole';

interface ColumnConfigProps {
  handleEdit: (row: any) => void;
  handleDelete: (row: any) => void;
  currentUserRole?: UserRole;
}

export const getUserColumns = ({
  handleEdit,
  handleDelete,
  currentUserRole,
}: ColumnConfigProps): TableColumn[] => [
  { key: 'firstName', label: 'First Name', align: 'left', width: '140px' },
  { key: 'lastName', label: 'Last Name', align: 'left', width: '140px' },
  {
    key: 'uniqueIdentifier',
    label: 'Unique Identifier',
    align: 'left',
    width: '140px',
  },
  { key: 'username', label: 'Username', align: 'left', width: '140px' },
  { key: 'email', label: 'Email', align: 'left', width: '200px' },
  { key: 'phone', label: 'Phone', align: 'left', width: '140px' },
  { key: 'role', label: 'Role', align: 'left', width: '140px' },
  {
    key: 'actions',
    label: '',
    align: 'right',
    width: '150px',
    render: (_value, row) => {
      // Check if current user is manager and target user is also manager
      const isEditDisabled =
        currentUserRole === UserRole.MANAGER && row.role === UserRole.MANAGER;

      return (
        <Box sx={{ display: 'flex', gap: 1, justifyContent: 'right' }}>
          <Button
            variant='outlined'
            color='secondary'
            size='small'
            disabled={isEditDisabled}
            onClick={() => handleEdit(row)}>
            Edit
          </Button>
          <Button
            variant='outlined'
            color='error'
            size='small'
            disabled={isEditDisabled}
            onClick={() => handleDelete(row)}>
            Delete
          </Button>
        </Box>
      );
    },
  },
];
