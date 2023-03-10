import { createAsyncThunk, createSlice, PayloadAction } from "@reduxjs/toolkit";

export interface AuthenticatedUser {
  firstName: string;
  lastName: string;
  email: string;
  role: string;
}

export interface AuthState {
  user: AuthenticatedUser | undefined;
}

export const fetchAuthUser = createAsyncThunk("user/current", async (_, _thunkApi) => {
  try {
    const response = await fetch("http://localhost:8080/api/v1/users/current");
    if (!response.ok) return undefined;

    const data = await response.json();
    console.log(data);
    const user = data as AuthenticatedUser;
    return user;
  } catch (err) {
    return undefined;
  }
});

export const authSlice = createSlice({
  name: "auth",
  initialState: {
    user: undefined,
  } as AuthState,
  reducers: {
    login(state, action: PayloadAction<AuthenticatedUser>) {
      state.user = action.payload;
    },
    logout(state) {
      state.user = undefined;
    },
  },
  extraReducers: (builder) => {
    builder.addCase(fetchAuthUser.fulfilled, (state, action) => {
      state.user = action.payload;
    });
  },
});

export const authActions = authSlice.actions;

