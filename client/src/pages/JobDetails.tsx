import { json, Link } from "react-router-dom";
import { jobApi } from "../services/jobs";
import { store } from "../store/store";

    // <>
    //   <h1>{job.title}</h1>
    //   <Link to={`/company/${job.company.name}/${job.company.id}`}>
    //     <h2>{job.company.name}</h2>
    //   </Link>
    //   <section>
    //     <ul>
    //       <li>Salary: {job.salaryFrom} - {job.salaryUpTo} {job.coin}</li>
    //       <li>Industry: {job.industry}</li>
    //       <li>Location: {job.location}</li>
    //       <li>Workday Type: {job.workdayType}</li>
    //       <li>Workplace Type: {job.workplaceType}</li>
    //     </ul>
    //   </section>
    //   <section>
    //     <p>{job.description}</p>
    //   </section>
    //   <section>
    //     <p>How to apply:</p>
    //     <p>{job.howToApply}</p>
    //   </section>
    //   <a href="_" type="button">Apply</a>
    // </>

const JobDetailsPage = () => {
  return (
  <>details page</>
  );
};

export default JobDetailsPage;

export async function loader() {
  const response = store.dispatch(jobApi.endpoints.getJob.initiate(1));
  console.log(response)

  try {
    const data = await response.unwrap();
    return data;
  } catch (error) {
    throw json({ message: "Couldn't get the information from the server" });
  } finally {
    response.unsubscribe();
  }
}
