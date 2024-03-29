import { NavLink } from "react-router-dom";
import styles from "./MainHeader.module.css";
import Navigation from "./Navigation";

function MainNavigation() {
  return (
    <header className={styles.container}>
      <div>
        <NavLink to="/">
          <strong>JobSearch</strong>
        </NavLink>
        <Navigation />
      </div>
    </header>
  );
}

export default MainNavigation;
