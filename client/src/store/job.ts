import { createSlice, PayloadAction } from "@reduxjs/toolkit";

export interface JobState {
  selectedJobId?: string;
}

const initialState: JobState = {
  selectedJobId: undefined
}

const jobSlice = createSlice({
  name: "job",
  initialState,
  reducers: {
    setSelectedJobId: (state, payload: PayloadAction<string>) => {
      state.selectedJobId = payload.payload;
    }
  }
})

export const { setSelectedJobId } = jobSlice.actions;

export default jobSlice;
