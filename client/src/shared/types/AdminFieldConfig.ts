export type AdminFieldConfig = {
  isPrimaryKey: boolean;
  isReadOnly: boolean;
  isWriteOnCreate: boolean;
  relationEntity?: string;
  type: string;
  name: string;
};

export type AdminEntityConfig = {
  isReadOnly: boolean;
  fields: AdminFieldConfig[];
};
