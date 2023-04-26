import { Link } from "react-router-dom";
import { Job } from "../../types/Job";
import TagList from "../Tags/TagList";
import styles from "./JobDetailView.module.css";

function JobDetailView({ job }: { job?: Job }) {
  if (!job) {
    return <section>No job selected</section>;
  }

  return (
    <section>
      <Link to={`/jobs/${job.id}`} target="_blank" rel="noopener noreferrer">
        <h2>{job.title}</h2>
      </Link>
      <h3>{job.companyName}</h3>
      <ul>
        <li>{job.location}</li>
        <li>{job.workday}</li>
        <li>{job.workModel}</li>
      </ul>
      <div>{job.role}</div>
      <img
        src={`${job.companyLogoUrl}`}
        alt={`Logo of the company ${job.companyName}`}
        width="90px"
        height="90px"
      />
      <div className={styles.salary}>
          Salary from:{" "}
          {`${job.salaryFrom || "NS/NC"} ${
            job.salaryCurrency || ""
          } Salary up to: ${job.salaryUpTo || "NS/NC"} ${
            job.salaryCurrency || ""
          }`}
      </div>
      <TagList tags={job.tags} />
      <p className={styles.jobDescription}>{job.description}</p>
    </section>
  );
}

export default JobDetailView;
