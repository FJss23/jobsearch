import { createSlice, PayloadAction } from "@reduxjs/toolkit";

export interface AuthenticatedUser {
  firstName: string,
  lastName: string,
  email: string,
  role: string,
}

export interface AuthState {
  user: AuthenticatedUser | undefined
}

export const authSlice = createSlice({
  name: "auth",
  initialState: {
    user: undefined
  } as AuthState,
  reducers: {
    login(state, action: PayloadAction<AuthenticatedUser>) {
      state.user = action.payload;
    },
    logout(state) {
      state.user = undefined;
    }
  },
});

export const authActions = authSlice.actions;
