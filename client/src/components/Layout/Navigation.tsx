import { useContext } from "react";
import { NavLink, useNavigate } from "react-router-dom";
import { AuthContext } from "../../store/authContext";
import styles from "./Navigation.module.css";

function Navigation() {
  const navigate = useNavigate();
  const { isLoggedIn } = useContext(AuthContext);

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
      <nav>
        <ul className={styles.navbar}>
          <li>
            <NavLink to="/login">Statistics</NavLink>
          </li>
          {!isLoggedIn && (
            <li className={styles.login}>
              <NavLink to="/login">Login</NavLink>
            </li>
          )}
          {isLoggedIn && (
            <li>
              <button onClick={logoutHandler}>Logout</button>
            </li>
          )}
        </ul>
      </nav>
  )
}

export default Navigation;
