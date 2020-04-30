import { Button, Grid, Typography } from '@material-ui/core';
import axios, { AxiosResponse } from 'axios';
import { Formik } from 'formik';
import React from 'react';
import { useHistory, useParams, useRouteMatch } from 'react-router-dom';
import { useDataApi } from '../../hooks/UseDataApi';
import { AdminEntityConfig, AdminFieldConfig } from '../types/AdminFieldConfig';
import { AdminField } from './AdminField';

export type MatchParams = {
  id: string;
};

export type AdminFormConfig = {
  url: string;
  config: AdminEntityConfig;
  id?: string;
  path?: string;
};

// type AdminEditFormProps = AdminFormConfig & AdminEntityConfig
type InitialValue = { [x: string]: string | any[] };
const AdminFormNew: React.FC<AdminFormConfig> = (props: AdminFormConfig) => {
  const { url, config, path } = props;
  const history = useHistory();

  const save = (data: any): void => {
    axios
      .post(`${url}`, data)
      .then((response: AxiosResponse<any>): void => {
        // TODO: need to send the route back or a service to extract it from the entity
        history.push(`${path}/${response.data.id}`);
      })
      .catch((error): void => console.log('error', error));
  };

  const submit = (data: any): void => {
    save(data);
  };

  const initialValues: InitialValue = config.fields.reduce((previousValue: InitialValue, field: AdminFieldConfig) => {
    previousValue[field.name] = field.type === 'TO_MANY' ? [] : '';
    return previousValue;
  }, {});

  return (
    <Formik onSubmit={(data: any): void => submit(data)} initialValues={initialValues}>
      {(formikProps): JSX.Element => (
        <form onReset={formikProps.handleReset} onSubmit={formikProps.handleSubmit}>
          <Grid container spacing={2}>
            {config.fields.map((row: AdminFieldConfig) => (
              <AdminField key={row.name} {...row} {...{ isReadOnly: (row.isReadOnly || row.isPrimaryKey) && !row.isWriteOnCreate }} />
            ))}
            <Grid item xs={12}>
              <Button type={'submit'} disabled={config.isReadOnly}>
                {'Save'}
              </Button>
            </Grid>
          </Grid>
        </form>
      )}
    </Formik>
  );
};

const AdminFormUpdate: React.FC<AdminFormConfig> = (props: AdminFormConfig) => {
  const { id, url, config, path } = props;
  const [data] = useDataApi<any>(`${url}/${id}`, {});
  const history = useHistory();

  const update = (data: any): void => {
    axios
      .put(`${url}/${id}`, data)
      .then((response: AxiosResponse<any>): void => {
        // TODO: after save the form disappears, probably because of re-routing
        // may just set form state since successful POST should return updated entity
        history.push(`${path}/${response.data.id}`);
      })
      .catch((error): void => console.log('error'));
  };

  const submit = (data: any): void => {
    update(data);
  };

  const deleteRecord = (): void => {
    axios
      .delete(`${url}/${id}`)
      .then((response: AxiosResponse<any>): void => {
        history.push(`${path}`);
      })
      .catch((error): void => console.log('error'));
  };

  if (!data || data.isLoading || !data.isComplete || !data.data) {
    return (
      <div>
        <Typography variant={'h4'}>Loading...</Typography>
      </div>
    );
  } else {
    return (
      <Formik onSubmit={(data: any): void => submit(data)} initialValues={data.data}>
        {(formikProps): JSX.Element => (
          <form onReset={formikProps.handleReset} onSubmit={formikProps.handleSubmit}>
            <Grid container spacing={2}>
              {config.fields.map((row: AdminFieldConfig) => (
                <AdminField key={row.name} {...row} {...{ isReadOnly: row.isReadOnly || row.isPrimaryKey }} />
              ))}
              <Grid item>
                <Grid container>
                  <Grid item>
                    <Button type={'submit'} disabled={config.isReadOnly}>
                      {'Save'}
                    </Button>
                  </Grid>
                  <Grid item>
                    <Button color={'secondary'} variant={'outlined'} disabled={config.isReadOnly} onClick={deleteRecord}>
                      {'Delete'}
                    </Button>
                  </Grid>
                </Grid>
              </Grid>
            </Grid>
          </form>
        )}
      </Formik>
    );
  }
};

export const AdminForm: React.FC<AdminFormConfig> = (props: AdminFormConfig) => {
  const { id } = useParams<MatchParams>();
  const { url, config } = props;

  if (id) {
    return <AdminFormUpdate {...props} id={id} />;
  } else {
    return <AdminFormNew {...props} />;
  }
};
