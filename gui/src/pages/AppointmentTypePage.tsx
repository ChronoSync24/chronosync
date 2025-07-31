import React, { useState, useEffect, useCallback } from 'react';
import {
  Box,
  Typography,
  IconButton,
  Theme,
  useTheme,
  Alert,
} from '@mui/material';
import ReactTable from '../components/ReactTable';
import Filters from '../components/Filters';
import DynamicModal from '../components/forms/DynamicModal';
import ConfirmationDialog from '../components/ConfirmationDialog';
import FilterAltOutlinedIcon from '@mui/icons-material/FilterAltOutlined';
import AddIcon from '@mui/icons-material/Add';
import { AppointmentType } from '../models/appointmentType/AppointmentType';
import {
  get as getAppointmentTypes,
  remove,
  create,
  update,
} from '../services/AppointmentTypeService';
import { PaginatedAppointmentTypeRequestDTO } from '../dtos/requests/PaginatedAppointmentTypeRequestDTO';
import { AppointmentTypeRequestDTO } from '../dtos/requests/AppointmentTypeRequestDTO';
import { PageableResponse } from '../models/BaseEntity';
import { getAppointmentTypeColumns } from '../configs/ColumnConfigs';
import { appointmentTypeFilterFields } from '../configs/FilterConfigs';
import { appointmentTypeFormFields } from '../configs/forms/AppointmentTypeFormConfig';
import { Currency } from '../models/appointmentType/Currency';

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

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isCreating, setIsCreating] = useState(false);

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
  const [isCreate, setIsCreate] = useState(false);

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

  const handleEdit = (row: any) => {
    setAppointmentType(row);
    handleOpenModal(false);
  };

  const handleOpenModal = (isCreate: boolean) => {
    setIsModalOpen(true);
    setIsCreate(isCreate);
    if (isCreate) {
      setAppointmentType(null); // Reset for create mode
    }
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

  const handleFilterChange = (key: string, value: any) => {
    setFilterValues((prev) => ({ ...prev, [key]: value }));
  };

  const handleFormSubmit = async (formData: Record<string, string>) => {
    setIsCreating(true);

    try {
      const requestData: AppointmentTypeRequestDTO = {
        id: isCreate ? 0 : appointmentType!.id,
        name: formData.name,
        durationMinutes: parseInt(formData.durationMinutes),
        price: parseFloat(formData.price),
        colorCode: formData.colorCode,
        currency: formData.currency as Currency,
      };

      if (isCreate) {
        await create(requestData);
      } else {
        await update(requestData);
      }

      setAlert({
        message: (
          <span>
            Appointment type{' '}
            <Typography component='span' sx={{ fontWeight: 'bold' }}>
              {formData.name}
            </Typography>{' '}
            was successfully {isCreate ? 'created' : 'updated'}.
          </span>
        ),
        severity: 'success',
      });

      setIsModalOpen(false);
      refetchData();
    } catch (error) {
      console.error(
        `Error ${isCreate ? 'creating' : 'updating'} appointment type:`,
        error
      );
      setAlert({
        message: `Failed to ${isCreate ? 'create' : 'update'} appointment type. Please try again.`,
        severity: 'error',
      });
    } finally {
      setIsCreating(false);
    }
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
            onClick={() => handleOpenModal(true)}
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

      <ConfirmationDialog
        open={isDeleteDialogOpen}
        onClose={handleCloseDeleteDialog}
        onConfirm={handleConfirmDelete}
        title='Confirm Deletion'
        loadingTitle='Deleting...'
        message={
          <>
            Are you sure you want to delete the appointment type{' '}
            <Typography component='span' sx={{ fontWeight: 'bold' }}>
              {appointmentType?.name}
            </Typography>
            ?
          </>
        }
        loadingMessage={
          <>
            Deleting appointment type{' '}
            <Typography component='span' sx={{ fontWeight: 'bold' }}>
              {appointmentType?.name}
            </Typography>
            ...
          </>
        }
        confirmText='Delete'
        cancelText='Cancel'
        confirmColor='error'
        loading={deleteLoading}
      />

      <DynamicModal
        open={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onSubmit={handleFormSubmit}
        onCancel={() => setIsModalOpen(false)}
        title={
          isCreate ? 'Create New Appointment Type' : 'Edit Appointment Type'
        }
        fields={appointmentTypeFormFields}
        isCreate={isCreate}
        isLoading={isCreating}
        initialValues={
          isCreate || !appointmentType ?
            {}
          : {
              name: appointmentType.name,
              durationMinutes: appointmentType.durationMinutes.toString(),
              price: appointmentType.price.toString(),
              colorCode: appointmentType.colorCode,
              currency: appointmentType.currency,
            }
        }
      />
    </Box>
  );
};

export default AppointmentTypePage;