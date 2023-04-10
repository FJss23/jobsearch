import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { User } from "../types/User";
import { RootState } from "./store";

export interface AuthState {
  user: User | undefined;
  token: string | undefined;
  isLoggedIn: boolean;
}

const initialState: AuthState = {
  user: undefined,
  token: undefined,
  isLoggedIn: false,
};

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    setCredentials: (state, payload: PayloadAction<AuthState>) => {
      state.user = payload.payload.user;
      state.token = payload.payload.token;
      state.isLoggedIn = payload.payload.isLoggedIn;
    },
    logout: () => initialState,
  },
});

export const { setCredentials, logout } = authSlice.actions;

export default authSlice;

export const selectCurrentUser = (state: RootState) => state.auth.user;
