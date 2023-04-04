import { JobOffer } from "./Job";
import JobCard from "./JobCard";
import styles from "./JobList.module.css";

export type JobsProps = {
  jobs: JobOffer[];
  title?: string;
};

const JobList = ({ jobs, title }: JobsProps) => {
  if (jobs.length === 0)
    return (
      <section>
        {title && <h2>{title}</h2>}
        <p>No jobs found</p>
      </section>
    );

  return (
    <section>
      <h2>{title}</h2>
      <div>
        <ul className={styles.jobList}>
          {jobs.map((job: JobOffer) => (
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
