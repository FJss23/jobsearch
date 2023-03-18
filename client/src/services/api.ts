import {
  FetchArgs,
  FetchBaseQueryError,
} from "@reduxjs/toolkit/dist/query/fetchBaseQuery";
import {
  BaseQueryFn,
  createApi,
  fetchBaseQuery,
  retry,
} from "@reduxjs/toolkit/query/react";
import { logout, tokenReceived, User } from "../store/auth";
import { RootState } from "../store/store";

export interface UserReponse {
  user: User;
  token: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

const baseQuery = fetchBaseQuery({
  baseUrl: "http://localhost:8080/api/v1",
  prepareHeaders: (headers, { getState }) => {
    const token = (getState() as RootState).auth.token;
    if (token) headers.set("Authorization", `Bearer ${token}`);
    return headers;
  },
});

const baseQueryWithRetry = retry(baseQuery, { maxRetries: 3 });

const baseQueryWithReAuth: BaseQueryFn<
  string | FetchArgs,
  unknown,
  FetchBaseQueryError
> = async (args, api, extraOptions) => {
  let result = await baseQueryWithRetry(args, api, extraOptions);

  if (result.error && result.error.status === 401) {
    const refreshResult = await baseQueryWithRetry(
      "/access-token",
      api,
      extraOptions
    );
    if (refreshResult.data) {
      api.dispatch(tokenReceived(refreshResult.data as string));
    } else {
      api.dispatch(logout());
    }
  }

  return result;
};

export const api = createApi({
  baseQuery: baseQueryWithReAuth,
  endpoints: (builder) => ({
    login: builder.mutation<UserReponse, LoginRequest>({
      query: (credentials) => ({
        url: "/auth/login",
        method: "POST",
        body: credentials,
      }),
    }),
  }),
  tagTypes: ["Jobs"],
});

export const { useLoginMutation } = api;
