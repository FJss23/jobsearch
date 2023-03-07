// import { JobCardDescription, Tag, Company } from "./Job";
import { JobDescription, JobsProps } from "./Job";
import JobCard from "./JobCard";

const JobList = ({ jobs }: JobsProps) => {
  if (jobs.length === 0) return <p>No jobs found</p>;

  return (
    <>
      <ul>
        {jobs.map((job: JobDescription) => (
          <li key={job.id}>
            <JobCard {...job} />
          </li>
        ))}
      </ul>
    </>
  );
};

export default JobList;
