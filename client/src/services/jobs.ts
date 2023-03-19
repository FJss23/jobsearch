import { JobOffer, JobOfferCard } from "../components/Jobs/Job";
import { api } from "./api";

export const jobApi = api.injectEndpoints({
  endpoints: (build) => ({
    getJobs: build.query<JobOfferCard[], void>({
      query: () => ({ url: "jobs" }),
      providesTags: (result = []) => [
        ...result.map(({ id }) => ({ type: "Jobs", id } as const)),
        { type: "Jobs" as const, id: "LIST" },
      ],
    }),
    getJob: build.query<JobOffer, number>({
      query: (id) => `jobs/${id}`,
      providesTags: (_job, _err, id) => [{ type: "Jobs", id }],
    }),
    addJob: build.mutation<JobOffer, Partial<JobOffer>>({
      query: (data) => ({
        url: "jobs",
        method: "POST",
        data,
      }),
      invalidatesTags: [{ type: "Jobs", id: "LIST" }],
    }),
    updateJob: build.mutation<JobOffer, Partial<JobOffer>>({
      query: (data) => ({
        url: "jobs",
        method: "PUT",
        data,
      }),
      invalidatesTags: (job) => [{ type: "Jobs", id: job?.id }],
    }),
    deleteJob: build.mutation<{ success: boolean; id: number }, number>({
      query: (id) => ({
        url: `jobs/${id}`,
        method: "DELETE",
      }),
      invalidatesTags: (job) => [{ type: "Jobs", id: job?.id }],
    }),
  }),
});

export const {
  useGetJobsQuery,
  useDeleteJobMutation,
  useUpdateJobMutation,
  useGetJobQuery,
} = jobApi;

