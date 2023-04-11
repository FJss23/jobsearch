import { configureStore } from "@reduxjs/toolkit";
import authSlice from "./auth";
import jobSlice from "./job";

const store = configureStore({
  reducer: {
    [authSlice.name]: authSlice.reducer,
    [jobSlice.name]: jobSlice.reducer,
  }
})

export { store };

// https://github.com/reduxjs/redux-toolkit/blob/master/examples/action-listener/counter/src/store.ts
export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
