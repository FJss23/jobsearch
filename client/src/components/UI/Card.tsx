import styles from "./Card.module.css";

const Card = ({ children }: { children: JSX.Element }) => {
  return <div className={styles.card}>{children}</div>;
};

export default Card;
