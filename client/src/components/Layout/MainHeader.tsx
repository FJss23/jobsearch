import { NavLink } from "react-router-dom";
import styles from "./MainHeader.module.css";
import Navigation from "./Navigation";

function MainNavigation() {
  return (
    <header className={styles.mainHeader}>
      <div>
        <NavLink to="/">JobSearch</NavLink>
      </div>
      <Navigation />
    </header>
  );
}

export default MainNavigation;
