import React, { useCallback, useEffect, useState } from "react";
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
import { useAppDisptach, useAppSelector } from "../hooks/hooks";
import { setSelectedJob } from "../store/job";
import { Job, Page } from "../types/Job";
import styles from "./Home.module.css";

function HomePage() {
  const { pageJobs, q } = useLoaderData() as {
    pageJobs: Page<Job>;
    q?: string;
  };
  const [search, setSearch] = useState(q || "");
  const selectedJob = useAppSelector((state) => state.job.selectedJob);
  const submit = useSubmit();
  const submitForm = useCallback((content: FormData) => submit(content), []);
  const navigation = useNavigation();
  const jobs = pageJobs.content
  const dispatch = useAppDisptach();

  const searching =
    navigation.location &&
    new URLSearchParams(navigation.location.search).has("q");

  useEffect(() => {
    const timer = setTimeout(() => {
      if (search) {
        console.log(search);
        const form = new FormData();
        form.set("q", search);
        dispatch(setSelectedJob(undefined));
        submitForm(form);
      }
    }, 1_000);

    return () => clearTimeout(timer);
  }, [search, submitForm, dispatch]);

  const searchHandler = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearch(e.target.value);
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
            placeholder="Company, Technology, Country, City or Keyword..."
            onChange={searchHandler}
            value={search}
          />
        </Form>
      </section>
      <hr />
      <div className={styles.loadingSpinner} aria-hidden hidden={!searching}>
        Loading...
      </div>
      {!searching && (
        <>
          <p
            className={styles.parameters}
            style={{
              textAlign: jobs.length === 0 ? "center" : "inherit",
            }}
          >
            <strong>{pageJobs.total}</strong> jobs <strong>{q}</strong> found
          </p>
          <div className={styles.jobsContainer}>
            <JobList
              jobs={jobs}
              prevPage={{
                visible: !pageJobs.first,
                link: `?${q ? "q=" + q + "&" : ""}from=${
                  pageJobs.prev
                }&size=${20}&page=prev`,
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
        </>
      )}
    </>
  );
}

export async function loader({ request }: LoaderFunctionArgs) {
  const url = new URL(request.url);
  const q = url.searchParams.get("q");
  const from = url.searchParams.get("from");
  const size = url.searchParams.get("size") || 20;
  const page = url.searchParams.get("page");

  let response;
  try {
    let params = `jobs?size=${size}`;
    if (q) params += `&search=${q}`;
    if (from) params += `&from=${from}`;
    if (page) params += `&page=${page}`;

    response = await fetch(`http://localhost:8080/api/v1/${params}`);

    const pageJobs = await response.json();
    console.log(pageJobs);
    return { pageJobs, q };
  } catch (error) {
    throw json({ message: "Couldn't get the information from the server" });
  }
}

export default HomePage;
