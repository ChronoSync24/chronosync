import React from 'react';
import { Box, Typography, Button } from '@mui/material';
import { TableColumn } from '../../components/ReactTable';
import { formatDuration, formatPrice } from '../../utils/formatters';

interface ColumnConfigProps {
  handleEdit: (row: any) => void;
  handleDelete: (row: any) => void;
}

export const getAppointmentTypeColumns = ({
  handleEdit,
  handleDelete,
}: ColumnConfigProps): TableColumn[] => [
  {
    key: 'id',
    label: 'ID',
    align: 'left',
    width: '40px',
  },
  {
    key: 'name',
    label: 'Name',
    align: 'left',
    width: '200px',
  },
  {
    key: 'durationMinutes',
    label: 'Duration',
    align: 'left',
    width: '100px',
    render: (value) => formatDuration(value),
  },
  {
    key: 'price',
    label: 'Price',
    align: 'left',
    width: '120px',
    render: (value, row) => formatPrice(value, row.currency),
  },
  {
    key: 'colorCode',
    label: 'Color',
    align: 'left',
    width: '150px',
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
        <Typography variant='body2'>{value}</Typography>
      </Box>
    ),
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
