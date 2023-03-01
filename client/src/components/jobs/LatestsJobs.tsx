import { JobDescription, Tag } from "./Job";
import JobCard from "./JobCard";

const tags: Tag[] = [
  { name: "Java" },
  { name: "Spring Boot" },
  { name: "Typescript" },
];

const jobs: JobDescription[] = [
  {
    jobId: "1",
    title: "Full Stack web developer",
    location: "San Francisco",
    remoteType: "Full Remote",
    companyName: "Some Random Compnay",
    createdAt: new Date(),
    imgUrl:
      "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fimages.fineartamerica.com%2Fimages%2Fartworkimages%2Fmediumlarge%2F2%2F1-sunny-day-in-spring-countryside-landscape-gone-with-the-wind.jpg&f=1&nofb=1&ipt=2ca679b984ac68797e7e05d6bbb8039bf149279fa922967f3ddab679729691b9&ipo=images",
    tags: tags,
  },
  {
    jobId: "2",
    title: "Full Stack web developer",
    location: "San Francisco",
    remoteType: "Full Remote",
    companyName: "Some Random Compnay",
    createdAt: new Date(),
    imgUrl:
      "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fimages.fineartamerica.com%2Fimages%2Fartworkimages%2Fmediumlarge%2F2%2F1-sunny-day-in-spring-countryside-landscape-gone-with-the-wind.jpg&f=1&nofb=1&ipt=2ca679b984ac68797e7e05d6bbb8039bf149279fa922967f3ddab679729691b9&ipo=images",
    tags: tags,
  },
];

const LatestsJobs = () => {
  return (
    <>
      <h2>Latests Jobs</h2>
      <ul>
        {jobs.map((job) => (
          <li key={job.jobId}>
            <JobCard {...job} />
          </li>
        ))}
      </ul>
    </>
  );
};

export default LatestsJobs;
