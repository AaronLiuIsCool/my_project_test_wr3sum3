import { createContext, useContext } from 'react';

export const I8nContext = createContext();

export function useI8n() {
  return useContext(I8nContext);
}
