import styles from "./Footer.module.css";

function Footer() {
  return (
    <footer className={styles.description}>
      JobSearch - {new Date().getFullYear()}
    </footer>
  );
}

export default Footer;
