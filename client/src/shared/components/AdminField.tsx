import { Chip, CircularProgress, Grid, TextField } from '@material-ui/core';
import { Autocomplete, UseAutocompleteMultipleProps, UseAutocompleteSingleProps } from '@material-ui/lab';
import { AutocompleteProps, RenderInputParams } from '@material-ui/lab/Autocomplete/Autocomplete';
import { DatePicker, DateTimePicker, TimePicker } from '@material-ui/pickers';
import axios from 'axios';
import { useField } from 'formik';
import React from 'react';
import { AdminFieldConfig } from '../types/AdminFieldConfig';

const AdminRelationField: React.FC<AdminFieldConfig> = (config: AdminFieldConfig) => {
  const [field, meta, helpers] = useField(config.name);
  const error: string = meta.error || '';
  const inErrorState: boolean = !!error && meta.touched;
  const disabled = config.isPrimaryKey || config.isReadOnly;
  const fieldLabel = field.name + (disabled ? ' (disabled)' : '');

  const [open, setOpen] = React.useState(false);
  const [options, setOptions] = React.useState<any[]>([]);
  const [query, setQuery] = React.useState<string | undefined>('');
  const [loading, setLoading] = React.useState<boolean>(false);

  // open and query listener
  React.useEffect(() => {
    if (!open) {
      setOptions([]);
      setLoading(false);
      return undefined;
    }

    setLoading(true);
    let active = true;
    let url = `/api/admin/${config.relationEntity}/autocomplete?size=20`;
    if (query) {
      url += `&q=${query}`;
    }

    (async (): Promise<void> => {
      const response = await axios.get(url);
      const relations = await response.data;
      // const options = relations.content.map((it: any) => it.id); // TODO: auto-generate this path on the UI
      const options = relations.content; // TODO: auto-generate this path on the UI
      if (active) {
        setOptions(options);
        setLoading(false);
      }
    })();

    return (): void => {
      active = false;
      setLoading(false);
    };
  }, [open, query, config.relationEntity]);

  const multiple: boolean = config.type === 'TO_MANY';
  const renderInput = (params: RenderInputParams): JSX.Element => {
    return (
      <TextField
        {...params}
        onChange={(event: React.ChangeEvent<HTMLInputElement>): void => {
          setQuery(event.target.value);
        }}
        // onBlur={(event?: React.FocusEvent<HTMLInputElement>): void => {
        //   const t = event?.target;
        //   if (t) {
        //     t.value = '';
        //   }
        // }}
        error={inErrorState}
        helperText={inErrorState ? error : ''}
        label={fieldLabel}
        variant="outlined"
        fullWidth
        InputLabelProps={{
          shrink: true,
        }}
        InputProps={{
          ...params.InputProps,
          endAdornment: (
            <React.Fragment>
              {loading ? <CircularProgress color="inherit" size={20} /> : null}
              {params.InputProps.endAdornment}
            </React.Fragment>
          ),
        }}
      />
    );
  };
  const autocompleteProps: AutocompleteProps<any> = {
    options: options,
    // onBlur: (event: React.FocusEvent<HTMLDivElement>): void => {
    //   event.target.nodeValue = null;
    //   helpers.setTouched(true);
    //   setOpen(false);
    // },
    disabled,
    loading: loading,
    renderTags: (value: any[], getTagProps: ({ index }: { index: number }) => {}): any => {
      return value.map(
        (option: any, index): JSX.Element => {
          return <Chip key={option.id} label={option.displayName} {...getTagProps({ index })} />;
        }
      );
    },
    renderInput,
  };
  const commonAutocompleteProps = {
    getOptionLabel: (option: any): string => {
      return option.displayName;
    },
    getOptionSelected: (option: any, value: any): boolean => {
      return option.id === value;
    },
    onClose: (): void => {
      setOpen(false);
      setQuery(undefined);
    },
    onOpen: (): void => {
      setOpen(true);
    },
    open: open,
    options: options,
  };

  if (multiple) {
    const autocompleteMultipleProps: UseAutocompleteMultipleProps<any> = {
      ...commonAutocompleteProps,
      onChange: (event: any, value: any[], reason): void => {
        helpers.setValue(value);
      },
      multiple: true,
    };

    return <Autocomplete {...field} {...autocompleteMultipleProps} {...autocompleteProps} />;
  } else {
    const autocompleteSingleProps: UseAutocompleteSingleProps<any> = {
      ...commonAutocompleteProps,
      onChange: (_: any, value: any | null): void => {
        helpers.setValue(value);
      },
    };

    return <Autocomplete {...field} {...autocompleteSingleProps} {...autocompleteProps} multiple={false} />;
  }
};

const AdminSingleField: React.FC<AdminFieldConfig> = (config: AdminFieldConfig) => {
  const [field, meta, helpers] = useField(config.name);
  const error: string = meta.error || '';
  const inErrorState: boolean = !!error && meta.touched;
  const disabled = config.isReadOnly;
  const fieldLabel = field.name + (disabled ? ' (disabled)' : '');

  const getField = (): JSX.Element => {
    switch (config.type) {
      case 'DATE_TIME':
        return (
          <DateTimePicker
            {...field}
            error={inErrorState}
            helperText={inErrorState ? error : ''}
            label={fieldLabel}
            inputVariant={'outlined'}
            InputLabelProps={{
              shrink: true,
            }}
            fullWidth
            disabled={disabled}
          />
        );
      case 'DATE':
        return (
          <DatePicker
            {...field}
            error={inErrorState}
            helperText={inErrorState ? error : ''}
            label={fieldLabel}
            inputVariant={'outlined'}
            InputLabelProps={{
              shrink: true,
            }}
            fullWidth
            disabled={disabled}
          />
        );
      case 'TIME':
        return (
          <TimePicker
            {...field}
            error={inErrorState}
            helperText={inErrorState ? error : ''}
            label={fieldLabel}
            inputVariant={'outlined'}
            InputLabelProps={{
              shrink: true,
            }}
            fullWidth
            disabled={disabled}
          />
        );
      case 'STRING':
      default:
        return (
          <TextField
            {...field}
            error={inErrorState}
            helperText={inErrorState ? error : ''}
            label={fieldLabel}
            variant={'outlined'}
            InputLabelProps={{
              shrink: true,
            }}
            fullWidth
            disabled={disabled}
            autoComplete={'off'}
          />
        );
    }
  };
  return getField();
};

export const AdminField: React.FC<AdminFieldConfig> = props => {
  const getField = (): JSX.Element => {
    if (props.type === 'TO_MANY' || props.type === 'TO_ONE') {
      return <AdminRelationField {...props} />;
    } else {
      return <AdminSingleField {...props} />;
    }
  };

  return (
    <Grid item xs={12}>
      {getField()}
    </Grid>
  );
};
