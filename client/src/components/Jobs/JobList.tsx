// import { JobCardDescription, Tag, Company } from "./Job";
import { JobOffer } from "./Job";
import JobCard from "./JobCard";

export type JobsProps = {
  jobs:  JobOffer[]
}

const JobList = ({ jobs }: JobsProps) => {
  if (jobs.length === 0) return <p>No jobs found</p>;

  return (
    <>
      <ul>
        {jobs.map((job: JobOffer) => (
          <li key={job.id}>
            <JobCard {...job} />
          </li>
        ))}
      </ul>
    </>
  );
};

export default JobList;
