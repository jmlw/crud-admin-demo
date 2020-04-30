import { Button, Grid, useTheme } from '@material-ui/core';
import IconButton from '@material-ui/core/IconButton';
import { createStyles, makeStyles, Theme } from '@material-ui/core/styles';
import Table from '@material-ui/core/Table';
import TableBody from '@material-ui/core/TableBody';
import TableCell from '@material-ui/core/TableCell';
import TableContainer from '@material-ui/core/TableContainer';
import TableHead from '@material-ui/core/TableHead';
import TablePagination from '@material-ui/core/TablePagination';
import TableRow from '@material-ui/core/TableRow';
import AddIcon from '@material-ui/icons/Add';
import FirstPageIcon from '@material-ui/icons/FirstPage';
import KeyboardArrowLeft from '@material-ui/icons/KeyboardArrowLeft';
import KeyboardArrowRight from '@material-ui/icons/KeyboardArrowRight';
import LastPageIcon from '@material-ui/icons/LastPage';
import React, { useEffect } from 'react';
import { useHistory, useRouteMatch } from 'react-router-dom';
import { useDataApi } from '../../hooks/UseDataApi';
import { get } from '../../util/object-util';
import { AdminEntityConfig } from '../types/AdminFieldConfig';

const useStyles1 = makeStyles((theme: Theme) =>
  createStyles({
    root: {
      flexShrink: 0,
      marginLeft: theme.spacing(2.5),
    },
  })
);

interface TablePaginationActionsProps {
  count: number;
  page: number;
  rowsPerPage: number;
  onChangePage: (event: React.MouseEvent<HTMLButtonElement>, newPage: number) => void;
}

function TablePaginationActions(props: TablePaginationActionsProps): JSX.Element {
  const classes = useStyles1();
  const theme = useTheme();
  const { count, page, rowsPerPage, onChangePage } = props;

  const handleFirstPageButtonClick = (event: React.MouseEvent<HTMLButtonElement>): void => {
    onChangePage(event, 0);
  };

  const handleBackButtonClick = (event: React.MouseEvent<HTMLButtonElement>): void => {
    onChangePage(event, page - 1);
  };

  const handleNextButtonClick = (event: React.MouseEvent<HTMLButtonElement>): void => {
    onChangePage(event, page + 1);
  };

  const handleLastPageButtonClick = (event: React.MouseEvent<HTMLButtonElement>): void => {
    onChangePage(event, Math.max(0, Math.ceil(count / rowsPerPage) - 1));
  };

  return (
    <div className={classes.root}>
      <IconButton onClick={handleFirstPageButtonClick} disabled={page === 0} aria-label="first page">
        {theme.direction === 'rtl' ? <LastPageIcon /> : <FirstPageIcon />}
      </IconButton>
      <IconButton onClick={handleBackButtonClick} disabled={page === 0} aria-label="previous page">
        {theme.direction === 'rtl' ? <KeyboardArrowRight /> : <KeyboardArrowLeft />}
      </IconButton>
      <IconButton onClick={handleNextButtonClick} disabled={page >= Math.ceil(count / rowsPerPage) - 1} aria-label="next page">
        {theme.direction === 'rtl' ? <KeyboardArrowLeft /> : <KeyboardArrowRight />}
      </IconButton>
      <IconButton onClick={handleLastPageButtonClick} disabled={page >= Math.ceil(count / rowsPerPage) - 1} aria-label="last page">
        {theme.direction === 'rtl' ? <FirstPageIcon /> : <LastPageIcon />}
      </IconButton>
    </div>
  );
}

type ColumnConfig<T> = {
  name: string;
  dataFn: (data: T) => any;
};
type ListPagination = {
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  rowsPerPage: number;
};
export type AdminListConfig<T> = {
  baseUrl: string;
  columnConfigs: ColumnConfig<T>[];
  rowIdFn: (row: T) => any;
};

type AdminListProps<T> = AdminListConfig<T> & AdminEntityConfig;
const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    root: {
      // width: '100%',
      // overflowX: 'auto',
      overflow: 'auto',
    },
    table: {
      // minWidth: 650,
    },
    pagination: {
      marginBottom: theme.spacing(8),
    },
    fab: {
      position: 'fixed',
      bottom: theme.spacing(2),
      right: theme.spacing(2),
    },
  })
);
export const AdminList: React.FC<AdminListProps<any>> = (props: AdminListProps<any>) => {
  const { columnConfigs, rowIdFn, baseUrl } = props;
  const [page, setPage] = React.useState(0);
  const [rowsPerPage, setRowsPerPage] = React.useState(10);
  const [state, setUrl] = useDataApi<any>(`${baseUrl}?size=${rowsPerPage}&page=${page}`, []);
  useEffect(() => {
    setUrl(`${baseUrl}?size=${rowsPerPage}&page=${page}`);
  }, [rowsPerPage, page, setUrl, baseUrl]);
  const data = get(state, 'data.content', []);
  const pagination = {
    totalElements: get(state, 'data.totalElements', 0),
    totalPages: get(state, 'data.totalPages', 1),
    size: get(state, 'data.size', 20),
    number: get(state, 'data.number', 0),
    rowsPerPage: rowsPerPage,
  };
  const classes = useStyles();
  const match = useRouteMatch();
  const history = useHistory();
  const handleClick = (event: any, id: number): void => {
    history.push(`${match.path}/${id}`);
  };

  const newRecord = (): void => {
    history.push(`${match.path}/new`);
  };
  const handleChangePage = (event: unknown, newPage: number): void => {
    setPage(newPage);
  };
  const handleChangeRowsPerPage = (event: React.ChangeEvent<HTMLInputElement>): void => {
    setRowsPerPage(parseInt(event.target.value, 10));
    setPage(0);
  };

  return (
    <React.Fragment>
      <Grid container wrap={'nowrap'} justify={'flex-end'}>
        <Grid item>
          <Button variant={'contained'} color={'primary'} aria-label="new" onClick={newRecord}>
            <AddIcon />
          </Button>
        </Grid>
      </Grid>
      <TableContainer>
        <Table className={classes.table} aria-label="simple table">
          <TableHead>
            <TableRow>
              {columnConfigs.map((config: ColumnConfig<any>) => (
                <TableCell key={config.name}>{config.name}</TableCell>
              ))}
            </TableRow>
          </TableHead>
          <TableBody>
            {data.map((row: any) => (
              <TableRow key={rowIdFn(row)} onClick={(event): void => handleClick(event, rowIdFn(row))}>
                {columnConfigs.map((config: ColumnConfig<any>) => (
                  <TableCell key={config.name} component="th" scope="row">
                    {config.dataFn(row)}
                  </TableCell>
                ))}
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      <TablePagination
        className={classes.pagination}
        rowsPerPageOptions={[10, 25, 100]}
        component="div"
        count={pagination.totalElements}
        rowsPerPage={pagination.rowsPerPage}
        page={pagination.number}
        onChangePage={handleChangePage}
        onChangeRowsPerPage={handleChangeRowsPerPage}
        ActionsComponent={TablePaginationActions}
      />
    </React.Fragment>
  );
};
