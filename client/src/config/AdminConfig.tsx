import BusinessIcon from '@material-ui/icons/Business';
import React from 'react';
import { DrawerRoute } from '../shared/types/DrawerRoute';

export const config: DrawerRoute[] = [
  {
    text: 'Features',
    path: 'features',
    api: 'api/admin/features',
    icon: (): JSX.Element => <BusinessIcon />,
    adminListConfig: {
      baseUrl: 'api/admin/features',
      columnConfigs: [
        {
          name: 'ID',
          dataFn: (data: any): string => data.id,
        },
        {
          name: 'Name',
          dataFn: (data: any): string => data.name,
        },
      ],
      rowIdFn: (row: any): string => row.id,
    },
  },
  {
    text: 'Rooms',
    path: 'rooms',
    api: 'api/admin/rooms',
    icon: (): JSX.Element => <BusinessIcon />,
    adminListConfig: {
      baseUrl: 'api/admin/rooms',
      columnConfigs: [
        {
          name: 'ID',
          dataFn: (data: any): string => data.id,
        },
        {
          name: 'Descirption',
          dataFn: (data: any): string => data.description,
        },
        {
          name: 'Floor',
          dataFn: (data: any): string => data.floor,
        },
        {
          name: 'Occupancy',
          dataFn: (data: any): string => data.occupancy,
        },
      ],
      rowIdFn: (row: any): string => row.id,
    },
  },
  {
    text: 'Hotels',
    path: 'hotels',
    api: 'api/admin/hotels',
    icon: (): JSX.Element => <BusinessIcon />,
    adminListConfig: {
      baseUrl: 'api/admin/hotels',
      columnConfigs: [
        {
          name: 'ID',
          dataFn: (data: any): string => data.id,
        },
        {
          name: 'Name',
          dataFn: (data: any): string => data.name,
        },
        {
          name: 'Address',
          dataFn: (data: any): string => data.address,
        },
        {
          name: 'Number of Rooms',
          dataFn: (data: any): string => data.rooms.length,
        },
      ],
      rowIdFn: (row: any): string => row.id,
    },
  },
];
