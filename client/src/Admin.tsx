import { Container } from '@material-ui/core';
import CssBaseline from '@material-ui/core/CssBaseline';
import { createStyles, makeStyles, Theme } from '@material-ui/core/styles';
import Typography from '@material-ui/core/Typography';
import React, { useEffect } from 'react';
import { Route, Switch, useRouteMatch } from 'react-router-dom';
import { config } from './config/AdminConfig';
import { AdminComponentRouter } from './shared/components/AdminComponentRouter';
import { AdminNav } from './shared/components/AdminNav';
import { DrawerRoute } from './shared/types/DrawerRoute';

const drawerWidth = 240;

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    root: {
      display: 'flex',
    },
    toolbar: theme.mixins.toolbar,
    content: {
      flexGrow: 1,
      padding: theme.spacing(3),
    },
  })
);

const Dashboard: React.FC<{setHeader: (name: string) => void}> = (props) => {
  const { setHeader } = props;

  useEffect(() => {
    setHeader("Dashboard");
  }, []);

  return (<Typography variant={'h2'}>Dashboard section</Typography>);
}

type AdminMainContentProps = {
  setHeader: (header: string) => void;
  config: DrawerRoute[];
};

const AdminMainContent: React.FC<AdminMainContentProps> = props => {
  const { setHeader, config } = props;
  const match = useRouteMatch();

  return (
    <Container>
      <Switch>
        <Route
          exact
          path={`${match.path}`}
          component={(): JSX.Element => {
            return <Dashboard setHeader={setHeader} />;
          }}
        />
        {config.map((c: DrawerRoute) => (
          <Route
            key={c.path}
            path={`${match.path}${c.path}`}
            component={(): JSX.Element => (
              <AdminComponentRouter listConfig={c.adminListConfig} api={c.api} setHeader={setHeader} name={c.text} />
            )}
          />
        ))}
      </Switch>
    </Container>
  );
};

export const Admin: React.FC = () => {
  const classes = useStyles();
  const [header, setHeader] = React.useState('Home');

  return (
    <div className={classes.root}>
      <CssBaseline />
      <AdminNav header={header} />
      <main className={classes.content}>
        <div className={classes.toolbar} />
        <AdminMainContent setHeader={setHeader} config={config} />
      </main>
    </div>
  );
};
