import {
  json,
  LoaderFunctionArgs,
  useLoaderData,
} from "react-router-dom";
import JobDetailView from "../components/Jobs/JobDetailView";
import { Job } from "../types/Job";

const JobDetailsPage = () => {
  const job = useLoaderData() as Job;

  return (
    <>
      <JobDetailView job={job} />
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
