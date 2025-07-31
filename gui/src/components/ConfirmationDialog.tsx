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

/**
 * Props for the ConfirmationDialog component
 * @param {boolean} open - Controls whether the dialog is visible
 * @param {() => void} onClose - Callback fired when dialog should close
 * @param {() => void} onConfirm - Callback fired when user confirms the action
 * @param {string} [title] - Dialog title text (default: "Confirm Action")
 * @param {string} [loadingTitle] - Title shown during loading state (default: "Processing...")
 * @param {string | React.ReactElement} message - Main message content displayed in dialog
 * @param {string | React.ReactElement} [loadingMessage] - Message shown during loading state
 * @param {string} [confirmText] - Text for the confirm button (default: "Confirm")
 * @param {string} [cancelText] - Text for the cancel button (default: "Cancel")
 * @param {'primary' | 'secondary' | 'error' | 'warning' | 'info' | 'success'} [confirmColor] - Color theme for confirm button (default: "primary")
 * @param {'text' | 'outlined' | 'contained'} [confirmVariant] - Style variant for confirm button (default: "contained")
 * @param {boolean} [loading] - Shows loading spinner and disables interaction
 * @param {boolean} [disabled] - Disables both action buttons
 */
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

/**
 * A reusable confirmation dialog component with loading state support.
 * Displays a modal dialog for user confirmation with customizable messaging,
 * button styling, and optional loading indicators.
 */
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
