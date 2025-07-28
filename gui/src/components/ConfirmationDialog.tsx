import React from 'react';
import {
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
  Button,
  CircularProgress,
  Box,
  Typography,
} from '@mui/material';

export interface ConfirmationDialogProps {
  open: boolean;
  onClose: () => void;
  onConfirm: () => void;
  title?: string;
  loadingTitle?: string;
  message: string | React.ReactElement;
  loadingMessage?: string | React.ReactElement;
  confirmText?: string;
  cancelText?: string;
  confirmColor?:
    | 'primary'
    | 'secondary'
    | 'error'
    | 'warning'
    | 'info'
    | 'success';
  confirmVariant?: 'text' | 'outlined' | 'contained';
  loading?: boolean;
  disabled?: boolean;
}

const ConfirmationDialog: React.FC<ConfirmationDialogProps> = ({
  open,
  onClose,
  onConfirm,
  title = 'Confirm Action',
  loadingTitle = 'Processing...',
  message,
  loadingMessage,
  confirmText = 'Confirm',
  cancelText = 'Cancel',
  confirmColor = 'primary',
  confirmVariant = 'contained',
  loading = false,
  disabled = false,
}) => {
  const handleClose = loading ? undefined : onClose;

  return (
    <Dialog open={open} onClose={handleClose}>
      <DialogTitle>{loading ? loadingTitle : title}</DialogTitle>
      <DialogContent>
        {loading && loadingMessage ?
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, py: 2 }}>
            <CircularProgress size={24} />
            <Typography>{loadingMessage}</Typography>
          </Box>
        : loading ?
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, py: 2 }}>
            <CircularProgress size={24} />
          </Box>
        : <>
            <DialogContentText component='div'>{message}</DialogContentText>
            <DialogContentText sx={{ mt: 1 }}>
              This action cannot be undone.
            </DialogContentText>
          </>
        }
      </DialogContent>
      <DialogActions sx={{ p: 2 }}>
        <Button
          onClick={onClose}
          color='primary'
          disabled={loading || disabled}>
          {cancelText}
        </Button>
        <Button
          onClick={onConfirm}
          color={confirmColor}
          variant={confirmVariant}
          disabled={loading || disabled}>
          {confirmText}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default ConfirmationDialog;
