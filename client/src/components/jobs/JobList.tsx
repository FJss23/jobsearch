import { JobCardDescription, Tag, Company } from "./Job";
import JobCard from "./JobCard";

const tags: Tag[] = [
  { id: "1", name: "Java" },
  { id: "2", name: "Spring Boot" },
  { id: "3", name: "Typescript" },
];

const company: Company = {
  id: "1",
  name: "Best company in the world",
  description: "This is the best company in the world, and you know it",
  logoUrl: "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fimages.fineartamerica.com%2Fimages%2Fartworkimages%2Fmediumlarge%2F2%2F1-sunny-day-in-spring-countryside-landscape-gone-with-the-wind.jpg&f=1&nofb=1&ipt=2ca679b984ac68797e7e05d6bbb8039bf149279fa922967f3ddab679729691b9&ipo=images",
}

const jobs: JobCardDescription[] = [
  {
    id: "1",
    title: "Full Stack web developer",
    location: "San Francisco",
    workplaceType: "Full Remote",
    createdAt: new Date(),
    company: company,
    tags: tags,
  },
  {
    id: "2",
    title: "Mobile Android and Swift",
    location: "New York",
    workplaceType: "Full Remote",
    createdAt: new Date(),
    company: company,
    tags: tags,
  },
];

const JobList = () => {
  return (
    <>
      <ul>
        {jobs.map((job) => (
          <li key={job.id}>
            <JobCard {...job} />
          </li>
        ))}
      </ul>
    </>
  );
};

export default JobList;
