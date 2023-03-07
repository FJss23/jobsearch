import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";

export interface AuthState {
  isAuthenticated: boolean;
}

export const login = createAsyncThunk(
  "user/login",
  async (credentials: { email: string; password: string }, ext) => {
    try {
      const response = await fetch("http://localhost:8080/api/v1/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(credentials),
      });
      const data = response.json();
      return data;
    } catch (error) {
      return ext.rejectWithValue(error)
    }
  }
);

export const authSlice = createSlice({
  name: "auth",
  initialState: {
    isAuthenticated: false,
  } as AuthState,
  reducers: {},
  extraReducers: {
    [login.fulfilled]: (state: AuthState) => {
      state.isAuthenticated = true;
    },
    [login.rejected]: (state: AuthState) => {
      state.isAuthenticated = false;
    }
  }
});

export const authActions = authSlice.actions;
