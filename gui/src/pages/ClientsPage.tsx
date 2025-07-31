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
import { Client } from '../models/Client';
import {
  get as getClients,
  remove,
  create,
  update,
} from '../services/ClientService';
import { PaginatedClientRequestDTO } from '../dtos/requests/PaginatedClientRequestDTO';
import { ClientRequestDTO } from '../dtos/requests/ClientRequestDTO';
import { PageableResponse } from '../models/BaseEntity';
import { getClientColumns } from '../configs/columns/ClientColumnsConfig';
import { clientFilterFields } from '../configs/filters/ClientFilterConfig';
import { clientFormFields } from '../configs/forms/ClientFormConfig';
import { extractFormValues } from '../utils/formHelpers';

/**
 * Client page component.
 */
const ClientsPage: React.FC = () => {
  const theme: Theme = useTheme();

  const [clients, setClients] = useState<PageableResponse<Client>>({
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
  const [client, setClient] = useState<Client | null>(null);
  const [isCreate, setIsCreate] = useState(false);

  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedFilterValues(filterValues);
    }, 500);

    return () => {
      clearTimeout(handler);
    };
  }, [filterValues]);

  const fetchClients = useCallback(
    (filters = debouncedFilterValues, page = currentPage, size = pageSize) => {
      setLoading(true);

      const request: PaginatedClientRequestDTO = {
        firstName: filters.firstName || '',
        lastName: filters.lastName || '',
        email: filters.email || '',
        phone: filters.phone || '',
        uniqueIdentifier: filters.uniqueIdentifier || '',
        page: page,
        pageSize: size,
      };

      getClients(request)
        .then((result) => {
          setClients(result);
        })
        .catch((error) => {
          console.error('Error fetching clients:', error);
          setAlert({
            message: 'Failed to fetch clients. Please try again.',
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
    fetchClients();
  }, [fetchClients]);

  useEffect(() => {
    fetchClients();
  }, [fetchClients]);

  const handleDelete = (row: any) => {
    setClient(row);
    setIsDeleteDialogOpen(true);
  };

  const handleEdit = (row: any) => {
    setClient(row);
    handleOpenModal(false);
  };

  const handleOpenModal = (isCreate: boolean) => {
    setIsModalOpen(true);
    setIsCreate(isCreate);
    if (isCreate) {
      setClient(null); // Reset for create mode
    }
  };

  const handleConfirmDelete = () => {
    if (!client) return;

    setDeleteLoading(true);
    remove(client.id)
      .then(() => {
        setAlert({
          message: (
            <span>
              Client{' '}
              <Typography component='span' sx={{ fontWeight: 'bold' }}>
                {client.firstName} {client.lastName}
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
        console.error('Error deleting client:', error);
        setAlert({
          message: 'Failed to delete client. Please try again.',
          severity: 'error',
        });
        handleCloseDeleteDialog();
      });
  };

  const handleCloseDeleteDialog = () => {
    setIsDeleteDialogOpen(false);
    setDeleteLoading(false);
    setClient(null);
  };

  const handleFilterChange = (key: string, value: any) => {
    setFilterValues((prev) => ({ ...prev, [key]: value }));
  };

  const handleFormSubmit = async (formData: Record<string, string>) => {
    setIsCreating(true);

    try {
      const requestData: ClientRequestDTO = {
        id: isCreate ? 0 : client!.id,
        firstName: formData.firstName,
        lastName: formData.lastName,
        email: formData.email,
        phone: formData.phone,
        uniqueIdentifier: formData.uniqueIdentifier,
      };

      if (isCreate) {
        await create(requestData);
      } else {
        await update(requestData);
      }

      setAlert({
        message: (
          <span>
            Client{' '}
            <Typography component='span' sx={{ fontWeight: 'bold' }}>
              {formData.firstName} {formData.lastName}
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
        `Error ${isCreate ? 'creating' : 'updating'} client:`,
        error
      );
      setAlert({
        message: `Failed to ${isCreate ? 'create' : 'update'} client. Please try again.`,
        severity: 'error',
      });
    } finally {
      setIsCreating(false);
    }
  };

  const columns = getClientColumns({ handleEdit, handleDelete });

  return (
    <Box sx={{ p: 3 }}>
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
        }}>
        <Typography variant='h4' gutterBottom>
          Clients
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
        fields={clientFilterFields}
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
          data={clients}
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
            Are you sure you want to delete the client{' '}
            <Typography component='span' sx={{ fontWeight: 'bold' }}>
              {client?.firstName} {client?.lastName}
            </Typography>
            ?
          </>
        }
        loadingMessage={
          <>
            Deleting client{' '}
            <Typography component='span' sx={{ fontWeight: 'bold' }}>
              {client?.firstName} {client?.lastName}
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
        title={isCreate ? 'Create New Client' : 'Edit Client'}
        fields={clientFormFields}
        isCreate={isCreate}
        isLoading={isCreating}
        initialValues={
          isCreate || !client ? {} : extractFormValues(client, clientFormFields)
        }
      />
    </Box>
  );
};

export default ClientsPage;
