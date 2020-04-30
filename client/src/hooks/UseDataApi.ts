import axios from 'axios';
import { Dispatch, SetStateAction, useEffect, useReducer, useState } from 'react';

type Action<T> = { type: 'FETCH_INIT' } | { type: 'FETCH_SUCCESS'; payload: T } | { type: 'FETCH_FAILURE' };

type State<T> = {
  isComplete: boolean;
  isLoading: boolean;
  isError: boolean;
  data: T;
};

const dataFetchReducer = <T>(state: State<T>, action: Action<T>): State<T> => {
  switch (action.type) {
    case 'FETCH_INIT':
      return { ...state, isComplete: false, isLoading: true, isError: false };
    case 'FETCH_SUCCESS':
      return {
        ...state,
        isComplete: true,
        isLoading: false,
        isError: false,
        data: action.payload,
      };
    case 'FETCH_FAILURE':
      return {
        ...state,
        isComplete: true,
        isLoading: false,
        isError: true,
      };
    default:
      throw new Error();
  }
};

export const useDataApi = <T>(initialUrl: string, initialData: T): [State<T>, Dispatch<SetStateAction<string>>] => {
  const [url, setUrl] = useState(initialUrl);

  const [state, dispatch] = useReducer(dataFetchReducer, {
    isComplete: false,
    isLoading: false,
    isError: false,
    data: initialData,
  });

  useEffect(() => {
    let didCancel = false;

    const fetchData = async (): Promise<void> => {
      dispatch({ type: 'FETCH_INIT' });

      try {
        await delay(0);
        const result = await axios(url);

        if (!didCancel) {
          dispatch({ type: 'FETCH_SUCCESS', payload: result.data });
        }
      } catch (error) {
        if (!didCancel) {
          dispatch({ type: 'FETCH_FAILURE' });
        }
      }
    };

    fetchData();

    return (): void => {
      didCancel = true;
    };
  }, [url]);

  return [state as State<T>, setUrl];
};

const delay = (ms: number): Promise<void> => {
  return new Promise(resolve => setTimeout(resolve, ms));
};
