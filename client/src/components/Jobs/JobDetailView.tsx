import { Link } from "react-router-dom";
import { Job } from "../../types/Job";
import TagList from "../Tags/TagList";

function JobDetailView({ job }: { job?: Job }) {
  if (!job) {
    return <section>Selected a job</section>;
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
      <div>{`${job.salaryFrom} - ${job.salaryUpTo} ${job.salaryCurrency}`}</div>
      <TagList tags={job.tags} />
      <p>{job.description}</p>
    </section>
  );
}

export default JobDetailView;
