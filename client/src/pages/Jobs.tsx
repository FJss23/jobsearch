import JobList from "../components/jobs/JobList";
import SearchBar from "../components/jobs/SearchBar";

const JobsPage = () => {
  return (
    <>
      <h1>Jobs page</h1>
      <SearchBar />
      <JobList />
    </>
  );
};

export default JobsPage;
