import React from 'react';
import { Box, Button } from '@mui/material';
import { TableColumn } from '../../components/ReactTable';

interface ColumnConfigProps {
  handleEdit: (row: any) => void;
  handleDelete: (row: any) => void;
}

export const getClientColumns = ({
  handleEdit,
  handleDelete,
}: ColumnConfigProps): TableColumn[] => [
  {
    key: 'firstName',
    label: 'First Name',
    align: 'left',
    width: '150px',
  },
  {
    key: 'lastName',
    label: 'Last Name',
    align: 'left',
    width: '150px',
  },
  {
    key: 'email',
    label: 'Email',
    align: 'left',
    width: '200px',
  },
  {
    key: 'phone',
    label: 'Phone',
    align: 'left',
    width: '150px',
  },
  {
    key: 'uniqueIdentifier',
    label: 'Unique Identifier',
    align: 'left',
    width: '150px',
  },
  {
    key: 'actions',
    label: '',
    align: 'right',
    width: '150px',
    render: (_, row) => (
      <Box sx={{ display: 'flex', gap: 1, justifyContent: 'right' }}>
        <Button
          variant='outlined'
          color='secondary'
          size='small'
          onClick={() => handleEdit(row)}>
          Edit
        </Button>
        <Button
          variant='outlined'
          color='error'
          size='small'
          onClick={() => handleDelete(row)}>
          Delete
        </Button>
      </Box>
    ),
  },
];
