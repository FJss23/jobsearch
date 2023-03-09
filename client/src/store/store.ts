import { configureStore } from "@reduxjs/toolkit";
import { authActions, AuthenticatedUser, authSlice } from "./auth";

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
const getAuthenticatedUser = async () => {
  try {
    const response = await fetch("http://localhost:8080/api/v1/users/current");
    if (!response.ok) return undefined;

    const data = await response.json();
    console.log(data)
    const user = data as AuthenticatedUser;
    store.dispatch(authActions.login(user));
  } catch (err) {
    return undefined;
  }
};

(async () => getAuthenticatedUser())();
