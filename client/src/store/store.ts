import { configureStore } from "@reduxjs/toolkit";
import { authSlice, fetchAuthUser } from "./auth";

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
export type RootState = ReturnType<typeof store.getState>;

export type AppDispatch = typeof store.dispatch;

/*
 * After a page reload, the "storage" provided by redux is created again, maybe the
 * user was authenticated, let's check it out.
 * */
console.log("Checking if the user was authenticated");
// store.dispatch(fetchAuthUser());
