import { Hidden, IconButton } from '@material-ui/core';
import AppBar from '@material-ui/core/AppBar';
import { createStyles, makeStyles, Theme } from '@material-ui/core/styles';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import MenuIcon from '@material-ui/icons/Menu';
import React from 'react';

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    // root: {
    //   display: 'flex',
    // },
    appBar: {
      transition: theme.transitions.create(['margin', 'width'], {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
      }),
      zIndex: theme.zIndex.drawer + 1,
    },
    menuButton: {
      marginRight: theme.spacing(2),
    },
  })
);

type AdminAppBarProps = {
  toggleDrawer: () => void;
  header: string;
};
export const AdminAppBar: React.FC<AdminAppBarProps> = (props): JSX.Element => {
  const classes = useStyles();
  const { toggleDrawer, header } = props;

  return (
    <AppBar position="fixed" className={classes.appBar}>
      <Toolbar>
        <Hidden smUp implementation="css">
          <IconButton
            color="inherit"
            aria-label="open drawer"
            edge="start"
            onClick={(): void => toggleDrawer()}
            className={classes.menuButton}
          >
            <MenuIcon />
          </IconButton>
        </Hidden>
        <Hidden xsDown implementation="css">
          <Typography variant={'h6'} noWrap>
            {`Admin - ${header}`}
          </Typography>
        </Hidden>
        <Hidden smUp implementation={'css'}>
          <Typography variant="h6" noWrap>
            {header}
          </Typography>
        </Hidden>
      </Toolbar>
    </AppBar>
  );
};
