import { useLoaderData } from "react-router-dom";
import { JobCardDescription, JobDescription } from "../components/jobs/Job";
import JobList from "../components/jobs/JobList";
import SearchBar from "../components/jobs/SearchBar";
import PopularTechnologies from "../components/PopularTechnologies";

function HomePage() {
  const jobs = useLoaderData() as JobDescription[];

  return (
    <>
      <h1>Job Search</h1>
      <SearchBar />
      <JobList {...jobs} />
      <PopularTechnologies />
    </>
  );
}

export default HomePage;

export async function loader() {
  const response = await fetch("http://localhost:8080/api/v1/jobs");

  if (!response.ok) {
    console.log("error")
  } else {
    const resData = await response.json();
    return resData as JobCardDescription[];
  }
}
