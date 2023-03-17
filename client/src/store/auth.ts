import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { RootState } from "./store";

export interface User {
  firstName: string;
  email: string;
  role: string;
}

export interface AuthState {
  user: User | undefined;
  token: string | undefined;
}

const initialState: AuthState = {
  user: undefined,
  token: undefined,
};

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    setCredentials: (state, payload: PayloadAction<AuthState>) => {
      state.user = payload.payload.user;
      state.token = payload.payload.token;
    },
    logout: () => initialState,
  },
});

export const { setCredentials, logout } = authSlice.actions;

export default authSlice;

export const selectCurrentUser = (state: RootState) => state.auth.user;
