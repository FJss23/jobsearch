import { json, useLoaderData } from "react-router-dom";
import { JobOffer } from "../components/Jobs/Job";
import JobList from "../components/Jobs/JobList";
import SearchBar from "../components/Jobs/SearchBar";
import { jobApi } from "../services/jobs";
import { store } from "../store/store";

const JobsPage = () => {
  const jobs = useLoaderData() as JobOffer[];

  return (
    <>
      <h1>Jobs page</h1>
      <SearchBar />
      <JobList jobs={jobs} />
    </>
  );
};

export default JobsPage;

export async function loader() {
  const response = store.dispatch(jobApi.endpoints.getJobs.initiate());
  console.log(response)

  try {
    const data = await response.unwrap();
    return data;
  } catch (error) {
    throw json({ message: "Couldn't get the information from the server" });
  } finally {
    response.unsubscribe();
  }
}
