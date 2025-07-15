import React, { useEffect, useRef, useState } from 'react';
import { Box, IconButton, useTheme, Tooltip, Typography } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import ContentCopyIcon from '@mui/icons-material/ContentCopy';
import PaidIcon from '@mui/icons-material/AttachMoney';
import UnpaidIcon from '@mui/icons-material/MoneyOff';
import { motion, AnimatePresence } from 'framer-motion';

export interface RadialMenuOption {
  key: string;
  label: string;
  icon: React.ReactNode;
  onClick: () => void;
}

interface RadialMenuProps {
  position: { x: number; y: number };
  appointmentData?: {
    startDateTime: string;
    endDateTime: string;
    isPaid?: boolean;
    [key: string]: any;
  } | null;
  onOptionSelect: (optionKey: string) => void;
  onClose: () => void;
}

const getMenuOptions = (
  appointmentData: RadialMenuProps['appointmentData'],
  onOptionSelect: (optionKey: string) => void
): RadialMenuOption[] => {
  const now = new Date();
  if (!appointmentData) {
    return [
      {
        key: 'add',
        label: 'Add New Appointment',
        icon: <AddIcon />,
        onClick: () => onOptionSelect('add'),
      },
    ];
  }
  const end = new Date(appointmentData.endDateTime);
  const isPast = now > end;
  if (isPast) {
    return [
      {
        key: 'togglePaid',
        label: appointmentData.isPaid ? 'Mark Unpaid' : 'Mark Paid',
        icon: appointmentData.isPaid ? <UnpaidIcon /> : <PaidIcon />,
        onClick: () => onOptionSelect('togglePaid'),
      },
      {
        key: 'copy',
        label: 'Copy Appointment',
        icon: <ContentCopyIcon />,
        onClick: () => onOptionSelect('copy'),
      },
    ];
  } else {
    return [
      {
        key: 'edit',
        label: 'Edit Appointment',
        icon: <EditIcon />,
        onClick: () => onOptionSelect('edit'),
      },
      {
        key: 'delete',
        label: 'Delete Appointment',
        icon: <DeleteIcon />,
        onClick: () => onOptionSelect('delete'),
      },
      {
        key: 'copy',
        label: 'Copy Appointment',
        icon: <ContentCopyIcon />,
        onClick: () => onOptionSelect('copy'),
      },
    ];
  }
};

const RadialMenu: React.FC<RadialMenuProps> = ({ position, appointmentData, onOptionSelect, onClose }) => {
  const theme = useTheme();
  const menuRef = useRef<HTMLDivElement>(null);
  const options = getMenuOptions(appointmentData, onOptionSelect);
  const radius = 70;
  const [hovered, setHovered] = useState<string | null>(null);

  // Click-away and ESC close
  useEffect(() => {
    const handleClick = (e: MouseEvent) => {
      if (menuRef.current && !menuRef.current.contains(e.target as Node)) {
        onClose(); // Only close, do not move
      }
    };
    const handleEsc = (e: KeyboardEvent) => {
      if (e.key === 'Escape') onClose();
    };
    document.addEventListener('mousedown', handleClick);
    document.addEventListener('keydown', handleEsc);
    return () => {
      document.removeEventListener('mousedown', handleClick);
      document.removeEventListener('keydown', handleEsc);
    };
  }, [onClose]);

  // Arrange icons around the cursor, starting at 12 o'clock, clockwise
  const angleStep = (2 * Math.PI) / options.length;

  return (
    <AnimatePresence>
      <motion.div
        ref={menuRef}
        initial={{ scale: 0, opacity: 0 }}
        animate={{ scale: 1, opacity: 1 }}
        exit={{ scale: 0, opacity: 0 }}
        transition={{ type: 'spring', stiffness: 400, damping: 30 }}
        style={{
          position: 'fixed',
          left: position.x,
          top: position.y,
          zIndex: 1300,
          pointerEvents: 'auto',
        }}
      >
        <Box sx={{ position: 'relative', width: 0, height: 0 }}>
          {options.map((option, i) => {
            const angle = -Math.PI / 2 + i * angleStep; // 12 o'clock start
            const x = Math.cos(angle) * radius;
            const y = Math.sin(angle) * radius;
            return (
              <motion.div
                key={option.key}
                initial={{ scale: 0, opacity: 0, x: 0, y: 0 }}
                animate={{ scale: 1, opacity: 1, x, y }}
                exit={{ scale: 0, opacity: 0, x: 0, y: 0 }}
                transition={{ delay: 0.05 * i, type: 'spring', stiffness: 300, damping: 20 }}
                style={{
                  position: 'absolute',
                  left: 0,
                  top: 0,
                  display: 'flex',
                  flexDirection: 'column',
                  alignItems: 'center',
                  justifyContent: 'center',
                }}
              >
                <Tooltip
                  title={option.label}
                  placement="top"
                  arrow
                  open={hovered === option.key}
                  disableFocusListener
                  disableTouchListener
                >
                  <IconButton
                    onClick={option.onClick}
                    onMouseEnter={() => setHovered(option.key)}
                    onMouseLeave={() => setHovered(null)}
                    sx={{
                      background: '#6A5BCD',
                      color: '#fff',
                      boxShadow: 2,
                      width: 48,
                      height: 48,
                      '&:hover': {
                        background: '#6A5BCD',
                        color: '#fff',
                        filter: 'brightness(1.15)',
                      },
                    }}
                  >
                    {option.icon}
                  </IconButton>
                </Tooltip>
              </motion.div>
            );
          })}
        </Box>
      </motion.div>
    </AnimatePresence>
  );
};

export default RadialMenu;
