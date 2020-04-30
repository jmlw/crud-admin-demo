import { useMediaQuery, useTheme } from '@material-ui/core';
import React from 'react';
import { useRouteMatch } from 'react-router-dom';
import { AdminAppBar } from './AdminAppBar';
import { AdminDrawer } from './AdminDrawer';

type AdminNavProps = {
  header: string;
};
export const AdminNav: React.FC<AdminNavProps> = props => {
  const { header } = props;
  const match = useRouteMatch();
  const theme = useTheme();
  const [drawerOpen, setDrawerOpen] = React.useState(false);
  const isMobile = !useMediaQuery(theme.breakpoints.up('sm'));

  const toggleDrawer = (): void => {
    if (isMobile) {
      setDrawerOpen(!drawerOpen);
    }
  };

  return (
    <React.Fragment>
      <AdminAppBar toggleDrawer={toggleDrawer} header={header} />
      <AdminDrawer drawerOpen={drawerOpen} path={match.path} toggleDrawer={toggleDrawer} />
    </React.Fragment>
  );
};
