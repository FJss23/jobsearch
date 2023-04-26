import { Link } from "react-router-dom";
import { Job } from "../../types/Job";
import JobCard from "./JobCard";
import styles from "./JobList.module.css";

export type JobsProps = {
  jobs: Job[];
  title?: string;
  prevPage: { visible: boolean; link: string };
  nextPage: { visible: boolean; link: string };
};

const JobList = ({ jobs, title, prevPage, nextPage }: JobsProps) => {
  if (jobs.length === 0)
    return (
      <section>
        {title && <h2>{title}</h2>}
        <p>No jobs found</p>
      </section>
    );

  return (
    <section>
      {title && <h2 className={styles.listTitle}>{title}</h2>}
      <div className={styles.jobListContainer}>
        <ul className={styles.jobList}>
          {jobs.map((job: Job) => (
            <li key={job.id}>
              <JobCard {...job} />
            </li>
          ))}
        </ul>
      </div>
      <div className={styles.pagLinks}>
        {prevPage.visible && <Link to={prevPage.link}>prev</Link>}
        {nextPage.visible && <Link to={nextPage.link}>next</Link>}
      </div>
    </section>
  );
};

export default JobList;
