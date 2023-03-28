import { NavLink, useNavigate } from "react-router-dom";
import { useAppSelector } from "../../hooks/hooks";
import styles from "./MainNavigation.module.css";

function MainNavigation() {
  const navigate = useNavigate();

  const isAuth = useAppSelector((state) => state.auth.user);

  const logoutHandler = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/v1/auth/logout", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({}),
      });

      if (!response.ok) return console.log("Something went wrong", response);

      navigate("/");
    } catch (err) {
      console.log("You can't logout right now");
    }
  };

  return (
    <header className={styles.mainHeader}>
      <div>
        <NavLink to="/">JobSearch</NavLink>
      </div>
      <nav>
        <ul className={styles.navbar}>
          <li>
            <NavLink to="/jobs">Jobs</NavLink>
          </li>
          {!isAuth && (
            <li className={styles.login}>
              <NavLink to="/login">Login</NavLink>
            </li>
          )}
          {isAuth && (
            <li>
              <NavLink to="/applications">Applications</NavLink>
            </li>
          )}
          {isAuth && (
            <li>
              <button onClick={logoutHandler}>Logout</button>
            </li>
          )}
        </ul>
      </nav>
    </header>
  );
}

export default MainNavigation;
