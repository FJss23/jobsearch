import { useEffect, useState } from "react";
import { json, useLoaderData } from "react-router-dom";
import JobDetailView from "../components/Jobs/JobDetailView";
import JobList from "../components/Jobs/JobList";
import { useAppSelector } from "../hooks/hooks";
import { Job, Page } from "../types/Job";
import styles from "./Home.module.css";

function HomePage() {
  const pageJobs = useLoaderData() as Page<Job>;
  const [jobs, setJobs] = useState<Job[]>(pageJobs.content);
  const [selectedJob, setSelectedJob] = useState<Job | undefined>(
    pageJobs.content[0]
  );
  const [pageData, setPageData] = useState({ totalPages: pageJobs.totalPages });
  const selectedJobId = useAppSelector((state) => state.job.selectedJobId);

  useEffect(() => {
    setSelectedJob(jobs.find((job) => job.id === selectedJobId));
  }, [jobs, setSelectedJob, selectedJobId]);

  const submitHandler = (e: React.FormEvent) => {
    console.log("submitting...");
  };

  return (
    <>
      <section>
        <h1>Jobs in EU & UK</h1>
        <p>These jobs are from Hacker News and Reddit</p>
        <form onSubmit={submitHandler} className={styles.searchForm}>
          <label htmlFor="search" hidden>
            Job position, company name, tags or keywords
          </label>
          <input
            type="text"
            name="search"
            id="search"
            placeholder="Position, Company name, Tags or Keywords"
            className={styles.searchInput}
          />
          <button type="submit" className={styles.searchButton}>
            Search
          </button>
        </form>
      </section>
      <div className={styles.jobsContainer}>
        <JobList jobs={jobs} onPrevPage={""} onNextPage={""} />
        <JobDetailView job={selectedJob} />
      </div>
    </>
  );
}

export default HomePage;

export async function loader() {
  const response = await fetch("http://localhost:8080/api/v1/jobs?size=20");

  try {
    const data = await response.json();
    console.log("loader function from home.tsx - ", data);
    return data;
  } catch (error) {
    throw json({ message: "Couldn't get the information from the server" });
  }
}
