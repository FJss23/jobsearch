import { configureStore } from "@reduxjs/toolkit";
import { authSlice } from "./auth";

const store = configureStore({
  reducer: {
    [authSlice.name]: authSlice.reducer,
  },
});

export { store };

/*
* For more information:
* - Check the redux/toolkit documentation for examples
* - Visit https://github.com/reduxjs/redux-toolkit/blob/master/examples/action-listener/counter/src/store.ts
*/
export type RootState = ReturnType<typeof store.getState>

export type AppDispatch = typeof store.dispatch
