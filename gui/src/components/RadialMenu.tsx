import React from 'react';
import { Menu, MenuItem, MenuButton, MenuDivider } from '@szhsin/react-menu';
import '@szhsin/react-menu/dist/index.css';
import AddIcon from '@mui/icons-material/Add';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import ContentCopyIcon from '@mui/icons-material/ContentCopy';
import PaidIcon from '@mui/icons-material/AttachMoney';
import UnpaidIcon from '@mui/icons-material/MoneyOff';
import { getAppointmentState } from './calendar/utils';

export interface RadialMenuProps {
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
) => {
  const now = new Date();
  if (!appointmentData) {
    return [
      {
        key: 'add',
        label: 'Add New Appointment',
        onClick: () => onOptionSelect('add'),
      },
    ];
  }
  // Use getAppointmentState for logic
  const state = getAppointmentState(appointmentData as any);
  if (state === 'pre') {
    return [
      { key: 'edit', label: 'Edit Appointment', onClick: () => onOptionSelect('edit') },
      { key: 'delete', label: 'Delete Appointment', onClick: () => onOptionSelect('delete') },
      { key: 'copy', label: 'Copy Appointment', onClick: () => onOptionSelect('copy') },
    ];
  } else if (state === 'during') {
    return [
      { key: 'inProgress', label: 'Mark In Progress', onClick: () => onOptionSelect('inProgress') },
      { key: 'edit', label: 'Edit Appointment', onClick: () => onOptionSelect('edit') },
      { key: 'copy', label: 'Copy Appointment', onClick: () => onOptionSelect('copy') },
    ];
  } else {
    // post
    return [
      { key: 'togglePaid', label: appointmentData.isPaid ? 'Mark Unpaid' : 'Mark Paid', onClick: () => onOptionSelect('togglePaid') },
      { key: 'copy', label: 'Copy Appointment', onClick: () => onOptionSelect('copy') },
    ];
  }
};

const RadialMenu: React.FC<RadialMenuProps> = ({ position, appointmentData, onOptionSelect, onClose }) => {
  const options = getMenuOptions(appointmentData, onOptionSelect);

  // The menu is rendered at a fixed position using a custom MenuButton
  return (
    <Menu
      menuButton={<MenuButton style={{ display: 'none' }} />}
      menuStyle={{ position: 'fixed', left: position.x, top: position.y, minWidth: 180, zIndex: 1300 }}
      boundingBoxPadding="8"
      direction="right"
      transition
      viewScroll="close"
      menuClassName="radial-menu-context"
      portal
      unmountOnClose
    >
      {options.map(option => (
        <MenuItem key={option.key} onClick={option.onClick}>
          {option.label}
        </MenuItem>
      ))}
      {options.length > 1 && <MenuDivider />}
      <MenuItem key="close" onClick={onClose}>Close</MenuItem>
    </Menu>
  );
};

export default RadialMenu;
