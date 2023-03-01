import LatestsJobs from "../components/jobs/LatestsJobs";
import SearchBar from "../components/jobs/SearchBar";
import PopularTechnologies from "../components/PopularTechnologies";

function HomePage() {
  return (
    <>
      <h1>Job Search</h1>
      <SearchBar />
      <LatestsJobs />
      <PopularTechnologies />
    </>
  );
}

export default HomePage;
