import { useAppDisptach, useAppSelector } from "../../hooks/hooks";
import { setSelectedJob } from "../../store/job";
import { Job } from "../../types/Job";
import TagList from "../Tags/TagList";
import styles from "./JobCard.module.css";

const JobCard = (props: Job) => {
  const selectedJob = useAppSelector((state) => state.job.selectedJob);
  const dispatch = useAppDisptach();

  const setSelectedJobHandler = () => {
    dispatch(setSelectedJob(props));
  };

  const selectedStyle =
    selectedJob?.id === props.id
      ? styles.cardSelectedBorder
      : styles.cardDefaultBorder;

  return (
    <article className={`${styles.cardContainer} ${selectedStyle}`}>
      <header>
        <h3 className={styles.cardTitle} onClick={setSelectedJobHandler}>
          {props.title}
        </h3>
      </header>
      <ul className={styles.generalInfo}>
        <li>
          <strong>Company</strong>: {props.company.name}
        </li>
        <li>
          <strong>Date</strong>:{" "}
          {`${new Date(props.createdAt).toLocaleDateString()}`}
        </li>
        <li>
          <strong>Location</strong>: {props.location}
        </li>
        <li>
          <strong>Work model</strong>: {props.workModel.replace(" ", " - ")}
        </li>
      </ul>
      <ul>
        <TagList tags={props.tags} />
      </ul>
    </article>
  );
};

export default JobCard;
