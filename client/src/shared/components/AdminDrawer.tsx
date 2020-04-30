import { Hidden } from '@material-ui/core';
import Divider from '@material-ui/core/Divider';
import Drawer from '@material-ui/core/Drawer';
import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import { createStyles, makeStyles, Theme } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import DashboardIcon from '@material-ui/icons/Dashboard';
import HomeIcon from '@material-ui/icons/Home';
import React from 'react';
import { Link } from 'react-router-dom';
import { config } from '../../config/AdminConfig';
import { DrawerRoute } from '../types/DrawerRoute';

const drawerWidth = 240;

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    drawer: {
      [theme.breakpoints.up('sm')]: {
        width: drawerWidth,
        flexShrink: 0,
      },
    },
    drawerPaper: {
      width: drawerWidth,
    },
    drawerHeader: {
      ...theme.mixins.toolbar,
      padding: theme.spacing(2),
    },
  })
);

type AdminDrawerProps = {
  drawerOpen: boolean;
  toggleDrawer: () => void;
  path: string;
};
export const AdminDrawer: React.FC<AdminDrawerProps> = (props): JSX.Element => {
  const classes = useStyles();
  const { drawerOpen, toggleDrawer, path } = props;

  const drawerContent = (
    <React.Fragment>
      <Hidden xsDown implementation="css">
        <div className={classes.drawerHeader} />
      </Hidden>
      <Hidden smUp implementation="css">
        <Typography variant={'h4'}>Admin</Typography>
      </Hidden>
      <List>
        <Link
          to={`${path}`}
          onClick={(): void => {
            toggleDrawer();
          }}
        >
          <ListItem button key={'Dashboard'}>
            <ListItemIcon>
              <DashboardIcon />
            </ListItemIcon>
            <ListItemText primary={'Dashboard'} />
          </ListItem>
        </Link>
      </List>
      <Divider />
      <List>
        <a
          href={'https://www.google.com'}
          onClick={(): void => {
            toggleDrawer();
          }}
        >
          <ListItem button key={'Google App'}>
            <ListItemIcon>
              <HomeIcon />
            </ListItemIcon>
            <ListItemText primary={'Google App'} />
          </ListItem>
        </a>
      </List>
      <Divider />
      <List>
        {config.map((c: DrawerRoute) => (
          <Link key={c.path} to={`${path}${c.path}`} onClick={(): void => toggleDrawer()}>
            <ListItem button key={c.text}>
              {c.icon && <ListItemIcon>{c.icon()}</ListItemIcon>}
              <ListItemText primary={c.text} />
            </ListItem>
          </Link>
        ))}
      </List>
    </React.Fragment>
  );

  return (
    <nav className={classes.drawer} aria-label="mailbox folders">
      <Hidden smUp implementation="css">
        <Drawer
          // container={container}
          variant="temporary"
          anchor={'left'}
          open={drawerOpen}
          onClose={(): void => toggleDrawer()}
          classes={{
            paper: classes.drawerPaper,
          }}
          ModalProps={{
            keepMounted: true, // Better open performance on mobile.
          }}
        >
          {drawerContent}
        </Drawer>
      </Hidden>
      <Hidden xsDown implementation="css">
        <Drawer
          classes={{
            paper: classes.drawerPaper,
          }}
          variant="permanent"
          open
        >
          {drawerContent}
        </Drawer>
      </Hidden>
    </nav>
  );
};
