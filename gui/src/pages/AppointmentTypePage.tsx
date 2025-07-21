import React, { useState, useEffect, useCallback } from 'react';
import {
  Box,
  Typography,
  IconButton,
  Theme,
  useTheme,
  Alert,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
  Button,
  CircularProgress,
} from '@mui/material';
import ReactTable from '../components/ReactTable';
import Filters from '../components/Filters';
import FilterAltOutlinedIcon from '@mui/icons-material/FilterAltOutlined';
import AddIcon from '@mui/icons-material/Add';
import { AppointmentType } from '../models/appointmentType/AppointmentType';
import {
  get as getAppointmentTypes,
  remove,
} from '../services/AppointmentTypeService';
import { PaginatedAppointmentTypeRequestDTO } from '../dtos/requests/PaginatedAppointmentTypeRequestDTO';
import { PageableResponse } from '../models/BaseEntity';
import { getAppointmentTypeColumns } from '../configs/ColumnConfigs';
import { appointmentTypeFilterFields } from '../configs/FilterConfigs';

const AppointmentTypePage: React.FC = () => {
  const theme: Theme = useTheme();

  const [appointmentTypes, setAppointmentTypes] = useState<
    PageableResponse<AppointmentType>
  >({
    content: [],
    totalElements: 0,
    totalPages: 0,
    number: 0,
    size: 0,
    first: false,
    last: false,
    empty: false,
  });
  const [isFiltersOpen, setIsFiltersOpen] = useState(false);
  const [filterValues, setFilterValues] = useState<Record<string, any>>({});
  const [debouncedFilterValues, setDebouncedFilterValues] = useState<
    Record<string, any>
  >({});
  const [loading, setLoading] = useState(false);
  const [alert, setAlert] = useState<{
    message: string | React.ReactElement;
    severity: 'success' | 'error';
  } | null>(null);

  useEffect(() => {
    if (alert) {
      const timer = setTimeout(() => {
        setAlert(null);
      }, 5000);
      return () => clearTimeout(timer);
    }
  }, [alert]);

  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);

  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [deleteLoading, setDeleteLoading] = useState(false);
  const [appointmentType, setAppointmentType] =
    useState<AppointmentType | null>(null);

  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedFilterValues(filterValues);
    }, 500);

    return () => {
      clearTimeout(handler);
    };
  }, [filterValues]);

  const fetchAppointmentTypes = useCallback(
    (filters = debouncedFilterValues, page = currentPage, size = pageSize) => {
      setLoading(true);

      const request: PaginatedAppointmentTypeRequestDTO = {
        name: filters.name || '',
        page: page,
        pageSize: size,
      };

      getAppointmentTypes(request)
        .then((result) => {
          setAppointmentTypes(result);
        })
        .catch((error) => {
          console.error('Error fetching appointment types:', error);
          setAlert({
            message: 'Failed to fetch appointment types. Please try again.',
            severity: 'error',
          });
        })
        .finally(() => {
          setLoading(false);
        });
    },
    [debouncedFilterValues, currentPage, pageSize]
  );

  const refetchData = useCallback(() => {
    fetchAppointmentTypes();
  }, [fetchAppointmentTypes]);

  useEffect(() => {
    fetchAppointmentTypes();
  }, [fetchAppointmentTypes]);

  const handleDelete = (row: any) => {
    setAppointmentType(row);
    setIsDeleteDialogOpen(true);
  };

  const handleConfirmDelete = () => {
    if (!appointmentType) return;

    setDeleteLoading(true);
    remove(appointmentType.id)
      .then(() => {
        setAlert({
          message: (
            <span>
              Appointment type{' '}
              <Typography component='span' sx={{ fontWeight: 'bold' }}>
                {appointmentType.name}
              </Typography>{' '}
              was successfully deleted.
            </span>
          ),
          severity: 'success',
        });
        handleCloseDeleteDialog();
        refetchData();
      })
      .catch((error) => {
        console.error('Error deleting appointment type:', error);
        setAlert({
          message: 'Failed to delete appointment type. Please try again.',
          severity: 'error',
        });
        handleCloseDeleteDialog();
      });
  };

  const handleCloseDeleteDialog = () => {
    setIsDeleteDialogOpen(false);
    setDeleteLoading(false);
    setAppointmentType(null);
  };

  const handleEdit = (id: number) => {
    console.log('Edit appointment type:', id);
  };

  const handleFilterChange = (key: string, value: any) => {
    setFilterValues((prev) => ({ ...prev, [key]: value }));
  };

  const columns = getAppointmentTypeColumns({ handleEdit, handleDelete });

  return (
    <Box sx={{ p: 3 }}>
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
        }}>
        <Typography variant='h4' gutterBottom>
          Appointment Types
        </Typography>
        <Box>
          <IconButton
            onClick={() => setIsFiltersOpen(!isFiltersOpen)}
            sx={{
              borderRadius: '8px',
              marginRight: '8px',
              color: theme.palette.secondary.main,
              border: `1px solid ${isFiltersOpen ? theme.palette.secondary.main : 'transparent'}`,
              '&:hover': {
                backgroundColor: theme.palette.background.paper,
                border: `1px solid ${theme.palette.secondary.main}`,
              },
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
              },
            }}>
            <AddIcon fontSize='medium' />
          </IconButton>
        </Box>
      </Box>

      <Filters
        isOpen={isFiltersOpen}
        fields={appointmentTypeFilterFields}
        values={filterValues}
        onFilterChange={handleFilterChange}
        onClearFilters={() => setFilterValues({})}
      />

      {alert && (
        <Box sx={{ mt: 2 }}>
          <Alert onClose={() => setAlert(null)} severity={alert.severity}>
            {alert.message}
          </Alert>
        </Box>
      )}

      <Box sx={{ mt: 2 }}>
        <ReactTable
          isLoading={loading}
          columns={columns}
          data={appointmentTypes}
          onPageChange={(newPage) => setCurrentPage(newPage)}
          onPageSizeChange={(newPageSize) => setPageSize(newPageSize)}
        />
      </Box>

      <Dialog
        open={isDeleteDialogOpen}
        onClose={deleteLoading ? undefined : handleCloseDeleteDialog}>
        <DialogTitle>
          {deleteLoading ? 'Deleting...' : 'Confirm Deletion'}
        </DialogTitle>
        <DialogContent>
          {deleteLoading ?
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, py: 2 }}>
              <CircularProgress size={24} />
              <Typography>
                Deleting appointment type{' '}
                <Typography component='span' sx={{ fontWeight: 'bold' }}>
                  {appointmentType?.name}
                </Typography>
                ...
              </Typography>
            </Box>
          : <>
              <DialogContentText>
                Are you sure you want to delete the appointment type{' '}
                <Typography component='span' sx={{ fontWeight: 'bold' }}>
                  {appointmentType?.name}
                </Typography>
                ?
              </DialogContentText>
              <DialogContentText sx={{ mt: 1 }}>
                This action cannot be undone.
              </DialogContentText>
            </>
          }
        </DialogContent>
        <DialogActions sx={{ p: 2 }}>
          <Button
            onClick={handleCloseDeleteDialog}
            color='primary'
            disabled={deleteLoading}>
            Cancel
          </Button>
          <Button
            onClick={handleConfirmDelete}
            color='error'
            variant='contained'
            disabled={deleteLoading}>
            Delete
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
};

export default AppointmentTypePage;
