// import { JobCardDescription, Tag, Company } from "./Job";
import { JobDescription } from "./Job";
import JobCard from "./JobCard";

const JobList = (props: JobDescription) => {
  return (
    <>
      <ul>
        {props.jobs.map((job) => (
          <li key={job.id}>
            <JobCard {...job} />
          </li>
        ))}
      </ul>
    </>
  );
};

export default JobList;
