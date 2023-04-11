import { Link } from "react-router-dom";
import { useAppDisptach } from "../../hooks/hooks";
import { setSelectedJobId } from "../../store/job";
import { Job } from "../../types/Job";
import TagList from "../Tags/TagList";
import styles from "./JobCard.module.css";

const JobCard = (props: Job) => {
  const dispatch = useAppDisptach();

  const setSelectedJobHandler = () => {
    dispatch(setSelectedJobId(props.id))
  }

  return (
    <article className={styles.cardContainer}>
      <header>
        <h2 className={styles.cardTitle} onClick={setSelectedJobHandler}>
          {props.title}
        </h2>
        <p>{props.companyName}</p>
        <span>{`${props.createdAt}`}</span>
      </header>
      <img
        src={`${props.companyLogoUrl}`}
        alt={`Logo of the company ${props.companyName}`}
        width="45px"
        height="45px"
      />
      <div>
        <span>{props.location}</span>
        <span>{props.workModel}</span>
      </div>
      <ul className={styles.tags}>
        <TagList tags={props.tags} />
      </ul>
    </article>
  );
};

export default JobCard;
