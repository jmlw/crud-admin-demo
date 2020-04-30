import React, { useEffect } from 'react';
import { Route, Switch, useRouteMatch } from 'react-router-dom';
import { useDataApi } from '../../hooks/UseDataApi';
import { AdminEntityConfig } from '../types/AdminFieldConfig';
import { AdminForm, AdminFormConfig } from './AdminForm';
import { AdminList, AdminListConfig } from './AdminList';

type AdminComponentRouterProps = {
  api: string;
  listConfig: AdminListConfig<any>;
  setHeader: (header: string) => void;
  name: string;
};
export const AdminComponentRouter: React.FC<AdminComponentRouterProps> = props => {
  const match = useRouteMatch();
  const { api, listConfig, name, setHeader } = props;
  const [config] = useDataApi<AdminEntityConfig>(`${api}/config`, {
    isReadOnly: true,
    fields: [],
  });
  useEffect(() => {
    setHeader(name);
  }, [name, setHeader]);

  if (config.isLoading || !config.isComplete) {
    return null;
  } else {
    const adminFormConfig: AdminFormConfig = {
      url: api,
      config: {
        isReadOnly: config.data.isReadOnly,
        fields: config.data.fields,
      },
      path: match.path,
    };

    return (
      <Switch>
        <Route
          exact
          path={`${match.path}`}
          render={(params): JSX.Element => {
            return <AdminList {...listConfig} {...config.data} />;
          }}
        />
        <Route
          exact
          path={`${match.path}/new`}
          render={(params): JSX.Element => {
            return <AdminForm {...adminFormConfig} />;
          }}
        />
        <Route
          exact
          path={`${match.path}/:id`}
          render={(params): JSX.Element => {
            return <AdminForm {...adminFormConfig} />;
          }}
        />
      </Switch>
    );
  }
};
