import { Link } from "react-router-dom";
import { JobOffer, Company, Tag } from "../components/Jobs/Job";

const tags: Tag[] = [
  { id: "1", name: "Java" },
  { id: "2", name: "Spring Boot" },
  { id: "3", name: "Typescript" },
];

const company: Company = {
  id: "1",
  name: "Best company in the world",
  description: "This is the best company in the world, and you know it",
  logoUrl:
    "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fimages.fineartamerica.com%2Fimages%2Fartworkimages%2Fmediumlarge%2F2%2F1-sunny-day-in-spring-countryside-landscape-gone-with-the-wind.jpg&f=1&nofb=1&ipt=2ca679b984ac68797e7e05d6bbb8039bf149279fa922967f3ddab679729691b9&ipo=images",
};

const job: JobOffer = {
  id: "1",
  title: "Full Stack web developer",
  industry: "Software Consultancy",
  salaryFrom: 45000,
  salaryUpTo: 50000,
  coin: "€",
  location: "San Francisco",
  workdayType: "Full Time",
  description: "This is a very short description, and you know it",
  workplaceType: "On Site",
  createdAt: new Date(),
  company: company,
  tags: tags,
};

const JobDetailsPage = () => {
  return (
    <>
      <h1>{job.title}</h1>
      <Link to={`/company/${job.company.name}/${job.company.id}`}>
        <h2>{job.company.name}</h2>
      </Link>
      <section>
        <ul>
          <li>Salary: {job.salaryFrom} - {job.salaryUpTo} {job.coin}</li>
          <li>Industry: {job.industry}</li>
          <li>Location: {job.location}</li>
          <li>Workday Type: {job.workdayType}</li>
          <li>Workplace Type: {job.workplaceType}</li>
        </ul>
      </section>
      <section>
        <p>{job.description}</p>
      </section>
      <section>
        <p>How to apply:</p>
        <p>{job.howToApply}</p>
      </section>
      <a href="_" type="button">Apply</a>
    </>
  );
};

export default JobDetailsPage;
