import { createSlice, PayloadAction } from "@reduxjs/toolkit";

export interface AuthenticatedUser {
  firstName: string,
  lastName: string,
  email: string,
  role: string,
}

export interface AuthState {
  user: AuthenticatedUser | undefined;
  token: string | undefined;
  isAuthenticated: boolean;
}

const initialState: AuthState = {
  user: undefined,
  token: undefined,
  isAuthenticated: false,
};

export const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    login(state, action: PayloadAction<AuthenticatedUser>) {
      state.user = action.payload;
    },
    logout: () => initialState,
  },
  extraReducers: (builder) => {
    builder.addMatcher(()
  },
});

export const authActions = authSlice.actions;
