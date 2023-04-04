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
        src={"https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Ftse2.mm.bing.net%2Fth%3Fid%3DOIP.P6YMYbb6ML_ae3oWWaR45AHaFj%26pid%3DApi&f=1&ipt=909500b0178af76a24e54f25553d52eb9f6b4375284c4db73e4d7c6edde7f83f&ipo=images"}
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
