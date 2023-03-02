import { Link } from "react-router-dom";
import { JobCardDescription } from "./Job";

const JobCard: React.FC<JobCardDescription> = ({
  id,
  title,
  location,
  workplaceType,
  company,
  createdAt,
  tags,
}: JobCardDescription) => {
  const encodedTitle = encodeURIComponent(title)
    .replaceAll("/", "-");
  const encodedCompanyName = encodeURIComponent(company.name)
    .replaceAll("/", "-");

  return (
    <article>
      <header>
        <Link
          to={`/jobs/${encodedCompanyName}/${company.id}/${encodedTitle}/${id}`}
        >
          <h2>{title}</h2>
        </Link>
        <Link to={`/company/${encodedCompanyName}/${company.id}`}>
          <p>{company.name}</p>
        </Link>
        <span>{createdAt.getTime()}</span>
      </header>
      <img
        src={company.logoUrl}
        alt={`Logo of the company ${company.name}`}
        width="100px"
        height="100px"
      />
      <div>
        <span>{location}</span>
        <span>{workplaceType}</span>
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
