import { json, useLoaderData } from "react-router-dom";
import { JobOffer } from "../components/Jobs/Job";
import JobList from "../components/Jobs/JobList";
import Filters from "../components/Jobs/Filters";
import { jobApi } from "../services/jobs";
import { store } from "../store/store";
import styles from "./Jobs.module.css";

const JobsPage = () => {
  const jobs = useLoaderData() as JobOffer[];

  return (
    <>
      <h1>All Jobs Available</h1>
      <div className={styles.jobsLayout}>
        <Filters />
        <JobList jobs={jobs}  />
      </div>
    </>
  );
};

export default JobsPage;

export async function loader() {
  const response = store.dispatch(jobApi.endpoints.getJobs.initiate());
  console.log(response);

  try {
    const data = await response.unwrap();
    return data;
  } catch (error) {
    throw json({ message: "Couldn't get the information from the server" });
  } finally {
    response.unsubscribe();
  }
}
