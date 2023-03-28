import { json, useLoaderData } from "react-router-dom";
import { JobOffer } from "../components/Jobs/Job";
import JobList from "../components/Jobs/JobList";
import PopularTechnologies from "../components/StartingPage/PopularTechnologies";
import { jobApi } from "../services/jobs";
import { store } from "../store/store";
import styles from "./Home.module.css";

function HomePage() {
  const jobs = useLoaderData() as JobOffer[];

  const submitHandler = (e: React.FormEvent) => {
    console.log("submitting...");
  };

  return (
    <>
      <section>
        <h1>Work remotely from Europe.</h1>
        <p>Work from anywhere in Europe. Start now!</p>
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
      <JobList jobs={jobs} title={"Latests Jobs"} />
      <PopularTechnologies />
    </>
  );
}

export default HomePage;

export async function loader() {
  const response = store.dispatch(jobApi.endpoints.getJobs.initiate());

  try {
    const data = await response.unwrap();
    console.log(data);
    return data;
  } catch (error) {
    throw json({ message: "Couldn't get the information from the server" });
  } finally {
    response.unsubscribe();
  }
}
