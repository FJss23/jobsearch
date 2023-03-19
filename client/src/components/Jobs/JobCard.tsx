import { Link } from "react-router-dom";
import { JobOfferCard } from "./Job";

const JobCard = (props: JobOfferCard) => {
  return (
    <article>
      <header>
        <Link
          to={`/jobs/${props.id}`}
        >
          <h2>{props.title}</h2>
        </Link>
        <Link to={`/company/${props.company.id}`}>
          <p>{props.company.name}</p>
        </Link>
        <span>{props.createdAt.toString()}</span>
      </header>
      <img
        src={props.company.logoUrl}
        alt={`Logo of the company ${props.company.name}`}
        width="100px"
        height="100px"
      />
      <div>
        <span>{props.location}</span>
        <span>{props.workplaceSystem}</span>
      </div>
      <ul>
        {props.tags.map((tag) => (
          <Link key={tag.defaultName} to={`/${tag.defaultName}`}>
            <li>{tag.defaultName}</li>
          </Link>
        ))}
      </ul>
    </article>
  );
};

export default JobCard;
