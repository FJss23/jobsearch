import { Link } from "react-router-dom";
import { Job } from "../../types/Job";
import JobCard from "./JobCard";
import styles from "./JobList.module.css";

export type JobsProps = {
  jobs: Job[];
  title?: string;
  onPrevPage: string;
  onNextPage: string;
};

const JobList = ({ jobs, title, onPrevPage, onNextPage }: JobsProps) => {
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
      <div>
        <div>
          <Link to={onPrevPage}>prev</Link>
          <Link to={onNextPage}>next</Link>
        </div>
        <ul className={styles.jobList}>
          {jobs.map((job: Job) => (
            <li key={job.id}>
              <JobCard {...job} />
            </li>
          ))}
        </ul>
      </div>
    </section>
  );
};

export default JobList;
