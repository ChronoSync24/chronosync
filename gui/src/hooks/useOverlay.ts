import { useContext } from 'react';
import { OverlayContext } from '../App';

export function useOverlay() {
  return useContext(OverlayContext);
} 