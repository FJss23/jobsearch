import { Link } from "react-router-dom";
import { Job } from "../../types/Job";
import TagList from "../Tags/TagList";
import styles from "./JobDetailView.module.css";

function JobDetailView({ job }: { job?: Job }) {
  if (!job) {
    return <section></section>;
  }

  return (
    <article>
      <header className={styles.header}>
        <img
          className={styles.jobImg}
          src={`${job.company.logoUrl}`}
          alt={`Logo of the company ${job.company.name}`}
          width="90px"
          height="90px"
        />
        <Link to={`/jobs/${job.id}`} target="_blank" rel="noopener noreferrer">
          <h2 className={styles.jobTitle}>{job.title}</h2>
        </Link>
      </header>
      <TagList tags={job.tags} />
      <ul className={styles.generalInfo}>
        <li>
          <strong>Company:</strong> {job.company.name}
        </li>
        <li>
          <strong>Location</strong>: {job.location}
        </li>
        <li>
          <strong>Date</strong>:{" "}
          {`${new Date(job.createdAt).toLocaleDateString()}`}
        </li>
        <li>
          <strong>Work model</strong>: {job.workModel.replace(" ", " - ")}
        </li>
        <li>
          <strong>Workday</strong>: {job.workday.replace(" ", " - ")}
        </li>
        {job.salary.from && (
          <li>
            <strong>Salary from</strong>: {job.salary.currency}
            {job.salary.from}
          </li>
        )}
        {job.salary.upTo && (
          <li>
            <strong>Salary up to</strong>: {job.salary.currency}
            {job.salary.upTo}
          </li>
        )}
        {job.role && (
          <li>
            <strong>Role</strong>: {job.role}
          </li>
        )}
      </ul>
      <p className={styles.jobDescription}>{job.description}</p>
    </article>
  );
}

export default JobDetailView;
