import { Link } from "react-router-dom";
import { JobOfferCard } from "./Job";
import styles from "./JobCard.module.css";

const JobCard = (props: JobOfferCard) => {
  return (
    <article className={styles.cardContainer}>
      <header>
        <Link
          to={`/jobs/${props.id}`}
        >
          <h2 className={styles.cardTitle}>{props.title}</h2>
        </Link>
        <p>{props.companyName}</p>
        <span>{`${props.createdAt}`}</span>
      </header>
      <img
        src={`${props.companyLogoUrl}`}
        alt={`Logo of the company ${props.companyName}`}
        width="100px"
        height="100px"
      />
      <div>
        <span>{props.location}</span>
        <span>{props.workModel}</span>
      </div>
      <ul>
        {props.tags.map((tag) => (
          <Link key={tag.id} to={`/${tag.name}`}>
            <li>{tag.name}</li>
          </Link>
        ))}
      </ul>
    </article>
  );
};

export default JobCard;
