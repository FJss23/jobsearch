import { Link } from "react-router-dom";
import { JobDescription } from "./Job";

const JobCard: React.FC<JobDescription> = ({
  jobId,
  title,
  location,
  remoteType,
  companyName,
  createdAt,
  imgUrl,
  tags,
}: JobDescription) => {
  const encodedTitle = encodeURIComponent(title)
    .replaceAll("/", "-");
  const encodedCompanyName = encodeURIComponent(companyName)
    .replaceAll("/", "-");

  return (
    <article>
      <header>
        <Link
          to={`/jobs/${encodedCompanyName}/${encodedTitle}/${jobId}`}
        >
          <h2>{title}</h2>
        </Link>
        <Link to={`/jobs/${encodedCompanyName}`}>
          <p>{companyName}</p>
        </Link>
        <span>{createdAt.getTime()}</span>
      </header>
      <img
        src={imgUrl}
        alt={`Logo of the company ${companyName}`}
        width="100px"
        height="100px"
      />
      <div>
        <span>{location}</span>
        <span>{remoteType}</span>
      </div>
      <ul>
        {tags.map((tag) => (
          <Link key={tag.name} to={`/${tag.name}`}>
            <li>{tag.name}</li>
          </Link>
        ))}
      </ul>
    </article>
  );
};

export default JobCard;
