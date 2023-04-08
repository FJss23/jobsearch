import {
  json,
  LoaderFunctionArgs,
  useLoaderData,
} from "react-router-dom";
import { Job } from "../types/Job";

const JobDetailsPage = () => {
  const job = useLoaderData() as Job;

  return (
    <>
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
    </>
  );
};

export default JobDetailsPage;

export async function loader({ params }: LoaderFunctionArgs) {
  const { jobId } = params;

  if (!jobId) throw json({ message: "The id is required" });

  const response = await fetch(`http://localhost:8080/api/v1/jobs/${jobId}`);
  console.log(response);

  try {
    const data = await response.json();
    return data;
  } catch (error) {
    throw json({ message: "Couldn't get the information from the server" });
  }
}
