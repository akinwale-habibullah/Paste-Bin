import { configureStore } from '@reduxjs/toolkit'
import authReducer from './features/authSlice';
import pasteSlice from './features/pasteSlice';

export const store = configureStore({
  reducer: {
    auth: authReducer,
    pastes: pasteSlice
  },
})