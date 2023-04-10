import styles from "./Footer.module.css";

function Footer() {
  return (
    <footer className={styles.container}>
      <p>JobSearch - {new Date().getFullYear()}</p>
    </footer>
  );
}

export default Footer;
