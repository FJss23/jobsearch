import { json, useLoaderData } from "react-router-dom";
import { JobOffer } from "../components/Jobs/Job";
import JobList from "../components/Jobs/JobList";
import SearchBar from "../components/Jobs/SearchBar";
import PopularTechnologies from "../components/StartingPage/PopularTechnologies";

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
  const response = await fetch("http://localhost:8080/api/v1/jobs");

  if (!response.ok)
    throw json({ message: "Couldn't get the information from the server" });

  let data = []

  try {
    data = await response.json();
  } catch (error) {
    throw json({ message: "Couldn't convert the information from the server" });
  }

  return data
}
