import React from 'react';
import {
  Box,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  styled,
} from '@mui/material';

const StyledTableContainer = styled(TableContainer)(({ theme }) => ({
  '& .MuiTable-root': {
    borderSpacing: '0 8px',
    borderCollapse: 'separate',
  },
}));

const StyledTableRow = styled(TableRow)(({ theme }) => ({
  height: '44px',
  '& td': {
    padding: '4px 8px',
    borderTop: '2px solid #E0E0E0',
    borderBottom: '2px solid #E0E0E0',
    backgroundColor: theme.palette.background.paper,
    '&:first-of-type': {
      borderTopLeftRadius: '8px',
      borderBottomLeftRadius: '8px',
      borderLeft: '2px solid #E0E0E0',
    },
    '&:last-of-type': {
      borderTopRightRadius: '8px',
      borderBottomRightRadius: '8px',
      borderRight: '2px solid #E0E0E0',
    },
  },
}));

const StyledHeaderRow = styled(TableRow)(({ theme }) => ({
  height: '44px',
  '& th': {
    padding: '4px 8px',
    borderLeft: 'none',
    borderRight: 'none',
    borderTop: '2px solid #E0E0E0',
    borderBottom: '2px solid #E0E0E0',
    backgroundColor: theme.palette.grey[100],
    fontWeight: 600,
    '&:first-of-type': {
      borderTopLeftRadius: '8px',
      borderBottomLeftRadius: '8px',
      borderLeft: '2px solid #E0E0E0',
    },
    '&:last-of-type': {
      borderTopRightRadius: '8px',
      borderBottomRightRadius: '8px',
      borderRight: '2px solid #E0E0E0',
    },
  },
}));

export interface TableColumn {
  key: string;
  label: string;
  align?: 'left' | 'center' | 'right';
  width?: string | number;
  render?: (value: any, row: any) => React.ReactNode;
}

export interface ReusableTableProps {
  columns: TableColumn[];
  data: any[];
  onRowClick?: (row: any) => void;
}

const ReactTable: React.FC<ReusableTableProps> = ({
  columns,
  data,
  onRowClick,
}) => {
  return (
    <Box sx={{ 
      '& .MuiTableContainer-root': { 
        backgroundColor: 'transparent',
        boxShadow: 'none',
      }
    }}>
      <StyledTableContainer sx={{ backgroundColor: 'transparent' }}>
        <Table>
          <TableHead>
            <StyledHeaderRow>
              {columns.map((column) => (
                <TableCell 
                  key={column.key} 
                  align={column.align || 'left'}
                  sx={{ width: column.width }}
                >
                  {column.label}
                </TableCell>
              ))}
            </StyledHeaderRow>
          </TableHead>
          <TableBody>
            {data.length === 0 ? (
              <StyledTableRow>
                <TableCell colSpan={columns.length} align="center">
                  No data available.
                </TableCell>
              </StyledTableRow>
            ) : (
              data.map((row, index) => (
                <StyledTableRow
                  key={row.id ?? index} // Prefer unique id if available
                  onClick={() => onRowClick?.(row)}
                  sx={{ cursor: onRowClick ? 'pointer' : 'default' }}
                >
                  {columns.map((column) => (
                    <TableCell 
                      key={column.key} 
                      align={column.align || 'left'}
                      sx={{ width: column.width }}
                    >
                      {column.render ? column.render(row[column.key], row) : row[column.key]}
                    </TableCell>
                  ))}
                </StyledTableRow>
              ))
            )}
          </TableBody>
        </Table>
      </StyledTableContainer>
    </Box>
  );
};

export default ReactTable; 