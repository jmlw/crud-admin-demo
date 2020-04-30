import { AdminListConfig } from '../components/AdminList';

export type DrawerRoute = {
  text: string;
  path: string;
  icon?: undefined | (() => JSX.Element);
  api: string;
  adminListConfig: AdminListConfig<any>;
};
