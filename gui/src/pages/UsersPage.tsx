import React, { useCallback, useEffect, useState } from 'react';
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
import { User } from '../models/user/User';
import { PageableResponse } from '../models/BaseEntity';
import {
  get as getUsers,
  remove,
  create,
  update,
} from '../services/UserService';
import { PaginatedUserRequestDTO } from '../dtos/requests/PaginatedUserRequestDTO';
import { UserRequestDTO } from '../dtos/requests/UserRequestDTO';
import { getUserColumns } from '../configs/columns/UserColumnsConfig';
import { userFilterFields } from '../configs/filters/UserFilterConfig';
import { FilterField } from '../components/Filters';
import { userFormFields } from '../configs/forms/UserFormConfig';
import { FieldConfig } from '../components/forms/FieldConfig';
import { extractFormValues } from '../utils/formHelpers';
import { getCurrentUserHighestRole } from '../utils/tokenUtils';
import { UserRole } from '../models/user/UserRole';

const UsersPage: React.FC = () => {
  const theme: Theme = useTheme();

  const highestRole = getCurrentUserHighestRole(localStorage.getItem('token'));

  // Filter role options for creation based on current user's authorization
  const getAuthorizedCreationRoleOptions = () => {
    const allRoleOptions = [
      { value: UserRole.ADMIN, label: 'Administrator' },
      { value: UserRole.MANAGER, label: 'Manager' },
      { value: UserRole.EMPLOYEE, label: 'Employee' },
    ];

    switch (highestRole) {
      case UserRole.ADMIN:
        return allRoleOptions; // Admin can create any role
      case UserRole.MANAGER:
        return allRoleOptions.filter(
          (option) => option.value === UserRole.EMPLOYEE
        ); // Manager can only create Employee
      default:
        return [];
    }
  };

  // Filter role options for filtering based on current user's authorization
  const getAuthorizedFilterRoleOptions = () => {
    const allRoleOptions = [
      { value: UserRole.ADMIN, label: 'Administrator' },
      { value: UserRole.MANAGER, label: 'Manager' },
      { value: UserRole.EMPLOYEE, label: 'Employee' },
    ];

    switch (highestRole) {
      case UserRole.ADMIN:
        return allRoleOptions; // Admin can filter all roles
      case UserRole.MANAGER:
        return allRoleOptions.filter(
          (option) =>
            option.value === UserRole.MANAGER ||
            option.value === UserRole.EMPLOYEE
        ); // Manager can filter Manager and Employee
      default:
        return allRoleOptions.filter(
          (option) => option.value === UserRole.EMPLOYEE
        );
    }
  };

  const isRoleEditingAllowed = () => {
    return highestRole === UserRole.ADMIN;
  };

  const getAuthorizedFormFields = (isCreateMode: boolean): FieldConfig[] =>
    userFormFields.reduce<FieldConfig[]>((acc, field) => {
      if (field.showOnCreate && !isCreateMode) return acc;
      if (field.name === 'role' && !isCreateMode && !isRoleEditingAllowed())
        return acc;

      acc.push(
        field.name === 'role' ?
          { ...field, options: getAuthorizedCreationRoleOptions() }
        : field
      );
      return acc;
    }, []);

  const authorizedFilterFields: FilterField[] = userFilterFields.map(
    (field) => {
      if (field.key === 'role') {
        return {
          ...field,
          options: getAuthorizedFilterRoleOptions(),
        };
      }
      return field;
    }
  );

  const [users, setUsers] = useState<PageableResponse<User>>({
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

  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize, setPageSize] = useState(10);

  const [isDeleteDialogOpen, setIsDeleteDialogOpen] = useState(false);
  const [deleteLoading, setDeleteLoading] = useState(false);
  const [user, setUser] = useState<User | null>(null);
  const [isCreate, setIsCreate] = useState(false);

  useEffect(() => {
    if (alert) {
      const timer = setTimeout(() => setAlert(null), 5000);
      return () => clearTimeout(timer);
    }
  }, [alert]);

  useEffect(() => {
    const handler = setTimeout(
      () => setDebouncedFilterValues(filterValues),
      500
    );
    return () => clearTimeout(handler);
  }, [filterValues]);

  const fetchUsers = useCallback(
    (filters = debouncedFilterValues, page = currentPage, size = pageSize) => {
      setLoading(true);

      const request: PaginatedUserRequestDTO = {
        firstName: filters.firstName || '',
        lastName: filters.lastName || '',
        email: filters.email || '',
        phone: filters.phone || '',
        uniqueIdentifier: filters.uniqueIdentifier || '',
        roles: filters.role ? [filters.role] : [],
        page: page,
        pageSize: size,
      };

      getUsers(request)
        .then((result) => setUsers(result))
        .catch((error) => {
          console.error('Error fetching users:', error);
          setAlert({
            message: 'Failed to fetch users. Please try again.',
            severity: 'error',
          });
        })
        .finally(() => setLoading(false));
    },
    [debouncedFilterValues, currentPage, pageSize]
  );

  const refetchData = useCallback(() => {
    fetchUsers();
  }, [fetchUsers]);

  useEffect(() => {
    fetchUsers();
  }, [fetchUsers]);

  const handleDelete = (row: any) => {
    setUser(row);
    setIsDeleteDialogOpen(true);
  };

  const handleEdit = (row: any) => {
    setUser(row);
    handleOpenModal(false);
  };

  const handleOpenModal = (isCreateFlag: boolean) => {
    setIsModalOpen(true);
    setIsCreate(isCreateFlag);
    if (isCreateFlag) setUser(null);
  };

  const handleConfirmDelete = () => {
    if (!user) return;
    setDeleteLoading(true);
    remove(user.id)
      .then(() => {
        setAlert({
          message: (
            <span>
              User{' '}
              <Typography component='span' sx={{ fontWeight: 'bold' }}>
                {user.firstName} {user.lastName}
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
        console.error('Error deleting user:', error);
        setAlert({
          message: 'Failed to delete user. Please try again.',
          severity: 'error',
        });
        handleCloseDeleteDialog();
      });
  };

  const handleCloseDeleteDialog = () => {
    setIsDeleteDialogOpen(false);
    setDeleteLoading(false);
    setUser(null);
  };

  const handleFilterChange = (key: string, value: any) => {
    setFilterValues((prev) => ({ ...prev, [key]: value }));
  };

  const handleFormSubmit = async (
    formData: Record<string, string | boolean>
  ) => {
    setIsCreating(true);
    try {
      const requestData: UserRequestDTO = {
        id: isCreate ? null : user!.id,
        firstName: formData.firstName as string,
        lastName: formData.lastName as string,
        uniqueIdentifier: formData.uniqueIdentifier as string,
        address: formData.address as string,
        phone: formData.phone as string,
        email: formData.email as string,
        ...(formData.password !== undefined && {
          password: formData.password as string,
        }),
        ...(formData.role !== undefined && { role: formData.role as UserRole }),
        isEnabled: formData.isEnabled as boolean,
      };

      if (isCreate) await create(requestData);
      else await update(requestData);

      setAlert({
        message: (
          <span>
            User{' '}
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
      console.error(`Error ${isCreate ? 'creating' : 'updating'} user:`, error);
      setAlert({
        message: `Failed to ${isCreate ? 'create' : 'update'} user. Please try again.`,
        severity: 'error',
      });
    } finally {
      setIsCreating(false);
    }
  };

  const columns = getUserColumns({
    handleEdit,
    handleDelete,
    currentUserRole: highestRole,
  });

  return (
    <Box sx={{ p: 3 }}>
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
        }}>
        <Typography variant='h4' gutterBottom>
          Users
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
        fields={authorizedFilterFields}
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
          data={users}
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
            Are you sure you want to delete the user{' '}
            <Typography component='span' sx={{ fontWeight: 'bold' }}>
              {user?.firstName} {user?.lastName}
            </Typography>
            ?
          </>
        }
        loadingMessage={
          <>
            Deleting user{' '}
            <Typography component='span' sx={{ fontWeight: 'bold' }}>
              {user?.firstName} {user?.lastName}
            </Typography>
            ...
          </>
        }
        confirmText='Delete'
        cancelText='Cancel'
        confirmColor='error'
        loading={deleteLoading}
      />
      {isModalOpen && (
        <DynamicModal
          open={isModalOpen}
          onClose={() => setIsModalOpen(false)}
          onSubmit={handleFormSubmit}
          onCancel={() => setIsModalOpen(false)}
          title={isCreate ? 'Create New User' : 'Edit User'}
          fields={getAuthorizedFormFields(isCreate)}
          isCreate={isCreate}
          isLoading={isCreating}
          initialValues={
            isCreate || !user ?
              {}
            : extractFormValues(user, getAuthorizedFormFields(isCreate))
          }
        />
      )}
    </Box>
  );
};

export default UsersPage;
