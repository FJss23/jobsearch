import { JobOfferCard } from "../components/Jobs/Job";
import { api } from "./api";

type JobsResponse = JobOfferCard[];

export const jobApi = api.injectEndpoints({
  endpoints: (build) => ({
    getJobs: build.query<JobsResponse, void>({
      query: () => ({ url: "jobs" }),
      providesTags: (result = []) => [
        ...result.map(({ id }) => ({ type: "Jobs", id } as const)),
        { type: "Jobs" as const, id: "LIST" },
      ],
    }),
  }),
});

export const { useGetJobsQuery } = jobApi;
