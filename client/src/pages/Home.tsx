import JobList from "../components/jobs/JobList";
import SearchBar from "../components/jobs/SearchBar";
import PopularTechnologies from "../components/PopularTechnologies";

function HomePage() {
  return (
    <>
      <h1>Job Search</h1>
      <SearchBar />
      <JobList />
      <PopularTechnologies />
    </>
  );
}

export default HomePage;
