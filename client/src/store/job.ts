import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { Job } from "../types/Job";

export interface JobState {
  selectedJob?: Job;
}

const initialState: JobState = {
  selectedJob: undefined
}

const jobSlice = createSlice({
  name: "job",
  initialState,
  reducers: {
    setSelectedJob: (state, payload: PayloadAction<Job | undefined>) => {
      state.selectedJob = payload.payload;
    }
  }
})

export const { setSelectedJob } = jobSlice.actions;

export default jobSlice;
