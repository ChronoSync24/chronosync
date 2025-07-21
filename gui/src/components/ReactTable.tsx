import React from 'react';
import {
  Box,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TablePagination,
  CircularProgress,
  styled,
} from '@mui/material';
import { PageableResponse } from '../models/BaseEntity';

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

export interface TableProps {
  isLoading: boolean;
  columns: TableColumn[];
  data: PageableResponse<any>;
  onRowClick?: (row: any) => void;
  onPageChange: (page: number) => void;
  onPageSizeChange: (pageSize: number) => void;
}

const ReactTable: React.FC<TableProps> = ({
  isLoading = false,
  columns,
  data,
  onRowClick,
  onPageChange,
  onPageSizeChange,
}) => {
  const handlePageChange = (event: unknown, newPage: number) => {
    onPageChange(newPage);
  };

  const handleRowsPerPageChange = (
    event: React.ChangeEvent<HTMLInputElement>
  ) => {
    onPageSizeChange(parseInt(event.target.value, 10));
  };

  return (
    <Box
      sx={{
        '& .MuiTableContainer-root': {
          backgroundColor: 'transparent',
          boxShadow: 'none',
        },
      }}>
      <StyledTableContainer sx={{ backgroundColor: 'transparent' }}>
        <Table sx={{ tableLayout: 'fixed' }}>
          <TableHead>
            <StyledHeaderRow>
              {columns.map((column) => (
                <TableCell
                  key={column.key}
                  align={column.align || 'left'}
                  sx={{ width: column.width }}>
                  {column.label}
                </TableCell>
              ))}
            </StyledHeaderRow>
          </TableHead>
          <TableBody>
            {isLoading ?
              <StyledTableRow>
                <TableCell
                  colSpan={columns.length}
                  align='center'
                  sx={{ py: 4 }}>
                  <CircularProgress size={24} />
                </TableCell>
              </StyledTableRow>
            : (data.content || []).map((row, index) => (
                <StyledTableRow
                  key={index}
                  onClick={() => onRowClick?.(row)}
                  sx={{ cursor: onRowClick ? 'pointer' : 'default' }}>
                  {columns.map((column) => (
                    <TableCell
                      key={column.key}
                      align={column.align || 'left'}
                      sx={{ width: column.width }}>
                      {column.render ?
                        column.render(row[column.key], row)
                      : row[column.key]}
                    </TableCell>
                  ))}
                </StyledTableRow>
              ))
            }
          </TableBody>
        </Table>
      </StyledTableContainer>

      <TablePagination
        component='div'
        count={data.totalElements}
        page={data.number}
        onPageChange={handlePageChange}
        rowsPerPage={data.size}
        onRowsPerPageChange={handleRowsPerPageChange}
        rowsPerPageOptions={[5, 10, 25, 50]}
        sx={{
          border: 'none',
          backgroundColor: 'inherit',
          '& .MuiTablePagination-toolbar': {
            backgroundColor: 'inherit',
            border: 'none',
            padding: 0,
          },
          '& .MuiTablePagination-selectLabel, & .MuiTablePagination-displayedRows':
            {
              margin: 0,
            },
        }}
      />
    </Box>
  );
};

export default ReactTable;
