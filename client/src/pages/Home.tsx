import React, { useEffect, useState } from "react";
import {
  Form,
  json,
  LoaderFunctionArgs,
  useLoaderData,
  useNavigation,
  useSubmit,
} from "react-router-dom";
import JobDetailView from "../components/Jobs/JobDetailView";
import JobList from "../components/Jobs/JobList";
import { useAppSelector } from "../hooks/hooks";
import { Job, Page } from "../types/Job";
import styles from "./Home.module.css";

function HomePage() {
  const { pageJobs, q } = useLoaderData() as {
    pageJobs: Page<Job>;
    q?: string;
  };
  const [jobs, setJobs] = useState<Job[]>([]);
  const [selectedJob, setSelectedJob] = useState<Job | undefined>(undefined);
  const submit = useSubmit();
  const navigation = useNavigation();
  const selectedJobId = useAppSelector((state) => state.job.selectedJobId);

  useEffect(() => {
    setSelectedJob(jobs.find((job) => job.id === selectedJobId));
    setJobs(pageJobs.content);
  }, [jobs, selectedJobId, pageJobs.content]);

  const searching =
    navigation.location &&
    new URLSearchParams(navigation.location.search).has("q");

  const searchHandler = (e: React.ChangeEvent<HTMLInputElement>) => {
    const isFirstSearch = q === null;
    submit(e.currentTarget.form, {
      replace:
        !isFirstSearch /* don't mess the browser history with every keystroke */,
    });
  };

  return (
    <>
      <section className={styles.jobSearch}>
        <h1>Jobs in EU & UK</h1>
        <h2>Jobs from Hacker News Who is Hiring montly post</h2>
        <Form className={styles.searchForm} id="search-form" role="search">
          <input
            className={styles.searchInput}
            type="search"
            aria-label="Search jobs"
            name="q"
            id="search"
            placeholder="Company, Technology or keyword... Start typing"
            onChange={searchHandler}
          />
        </Form>
      </section>
      <hr />
      <div className={styles.loadingSpinner} aria-hidden hidden={!searching}>
        Loading...
      </div>
      {!searching && (
        <div className={styles.jobsContainer}>
          <JobList
            jobs={jobs}
            prevPage={{
              visible: !pageJobs.first,
              link: `?q=${q}&from=${pageJobs.prev}&size=${20}`,
            }}
            nextPage={{
              visible: true,
              link: `?${q ? "q=" + q + "&" : ""}from=${
                pageJobs.next
              }&size=${20}`,
            }}
          />
          <JobDetailView job={selectedJob} />
        </div>
      )}
    </>
  );
}

export async function loader({ request }: LoaderFunctionArgs) {
  const url = new URL(request.url);
  const q = url.searchParams.get("q");
  const from = url.searchParams.get("from");
  const size = url.searchParams.get("size") || 20;
  console.log(q, from, size);

  let response;
  try {
    let params = `jobs?size=${size}`;
    if (q) params += `&search=${q}`;
    if (from) params += `&from=${from}`;

    response = await fetch(`http://localhost:8080/api/v1/${params}`);

    const pageJobs = await response.json();
    console.log(pageJobs);
    return { pageJobs, q };
  } catch (error) {
    throw json({ message: "Couldn't get the information from the server" });
  }
}

export default HomePage;
