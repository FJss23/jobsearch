import { useLoaderData } from "react-router-dom";
import { JobDescription } from "../components/jobs/Job";
import JobList from "../components/jobs/JobList";
import SearchBar from "../components/jobs/SearchBar";

const JobsPage = () => {
  const jobs = useLoaderData() as JobDescription;

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
  const response = await fetch("http://localhost:8080/api/v1/jobs");

  if (!response.ok) {
    console.log("error")
  } else {
    const resData = await response.json();
    return resData;
  }
}
