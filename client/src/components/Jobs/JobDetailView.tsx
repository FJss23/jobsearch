import { Job } from "../../types/Job";

function JobDetailView({ job }: { job: Job}) {
  return (
      <section>
        <h1>{job.title}</h1>
        <h2>{job.companyName}</h2>
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
        <ul>
          {job.tags.map((tag) => (
            <li key={tag.id}>{tag.name}</li>
          ))}
        </ul>
        <p>{job.description}</p>
      </section>
)
}

export default JobDetailView;
