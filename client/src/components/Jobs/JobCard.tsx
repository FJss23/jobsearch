import { Link } from "react-router-dom";
import { Job } from "../../types/Job";
import styles from "./JobCard.module.css";

const JobCard = (props: Job) => {
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
        width="90px"
        height="90px"
      />
      <div>
        <span>{props.location}</span>
        <span>{props.workModel}</span>
      </div>
      <ul className={styles.tags}>
        {props.tags.map((tag) => (
          <li key={tag.id}>
            <Link to="">{tag.name}</Link>
          </li>
        ))}
      </ul>
    </article>
  );
};

export default JobCard;
