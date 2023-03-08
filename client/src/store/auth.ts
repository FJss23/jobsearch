import { createSlice } from "@reduxjs/toolkit";

export interface AuthState {
  isAuthenticated: boolean;
}

export const authSlice = createSlice({
  name: "auth",
  initialState: {
    isAuthenticated: false,
  } as AuthState,
  reducers: {
    login(state) {
      state.isAuthenticated = true;
    },
    logout(state) {
      state.isAuthenticated = false;
    }
  },
});

export const authActions = authSlice.actions;
