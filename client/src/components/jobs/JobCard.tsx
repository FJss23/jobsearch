import { Link } from "react-router-dom";
import { JobCardDescription } from "./Job";

const JobCard = (props: JobCardDescription) => {
  const encodedTitle = encodeURIComponent(props.title).replaceAll("/", "-");
  const encodedCompanyName = encodeURIComponent(props.company.name).replaceAll(
    "/",
    "-"
  );

  return (
    <article>
      <header>
        <Link
          to={`/jobs/${encodedCompanyName}/${props.company.id}/${encodedTitle}/${props.id}`}
        >
          <h2>{props.title}</h2>
        </Link>
        <Link to={`/company/${encodedCompanyName}/${props.company.id}`}>
          <p>{props.company.name}</p>
        </Link>
        <span>{props.createdAt.getTime()}</span>
      </header>
      <img
        src={props.company.logoUrl}
        alt={`Logo of the company ${props.company.name}`}
        width="100px"
        height="100px"
      />
      <div>
        <span>{props.location}</span>
        <span>{props.workplaceType}</span>
      </div>
      <ul>
        {props.tags.map((tag) => (
          <Link key={tag.name} to={`/${tag.name}`}>
            <li>{tag.name}</li>
          </Link>
        ))}
      </ul>
    </article>
  );
};

export default JobCard;
