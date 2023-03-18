import { json, useLoaderData } from "react-router-dom";
import { JobOffer } from "../components/Jobs/Job";
import JobList from "../components/Jobs/JobList";
import SearchBar from "../components/Jobs/SearchBar";
import PopularTechnologies from "../components/StartingPage/PopularTechnologies";
import { jobApi } from "../services/jobs";
import { store } from "../store/store";

function HomePage() {
  const jobs = useLoaderData() as JobOffer[];

  return (
    <>
      <h1>Job Search</h1>
      <SearchBar />
      <JobList jobs={jobs} />
      <PopularTechnologies />
    </>
  );
}

export default HomePage;

export async function loader() {
  const response = store.dispatch(jobApi.endpoints.getJobs.initiate());

  try {
    const data = await response.unwrap();
    console.log(data)
    return data;
  } catch (error) {
    throw json({ message: "Couldn't get the information from the server" });
  } finally {
    response.unsubscribe();
  }
}
