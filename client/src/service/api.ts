import { createApi } from "@reduxjs/toolkit/dist/query";
import { RetryOptions } from "@reduxjs/toolkit/dist/query/retry";
import { fetchBaseQuery, retry } from "@reduxjs/toolkit/query/react";
import { RootState } from "../store/store";

const baseQuery = fetchBaseQuery({
  baseUrl: "/",
  prepareHeaders: (headers, { getState }) => {
    const token = (getState() as RootState).auth.token;
    if (token) {
      headers.set("Authentication", `Bearer ${token}`);
    }
    return headers;
  },
});

const retries: RetryOptions = { maxRetries: 6 };
const baseQueryWithRetry = retry(baseQuery, retries);

export const api = createApi({
  baseQuery: baseQueryWithRetry,
  tagTypes: ["Jobs"],
  endpoints: () => ({}),
});
