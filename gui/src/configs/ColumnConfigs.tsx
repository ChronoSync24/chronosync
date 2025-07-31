import React from 'react';
import { Box, Typography, Button } from '@mui/material';
import { TableColumn } from '../components/ReactTable';
import { Currency } from '../models/appointmentType/Currency';

interface ColumnConfigProps {
  handleEdit: (row: any) => void;
  handleDelete: (row: any) => void;
}

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
