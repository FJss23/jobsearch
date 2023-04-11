import { NavLink, useNavigate } from "react-router-dom";
import { useAppDisptach, useAppSelector } from "../../hooks/hooks";
import { logout } from "../../store/auth";
import { fetcher } from "../../util/fetcher";
import styles from "./Navigation.module.css";

function Navigation() {
  const navigate = useNavigate();
  const dispatch = useAppDisptach();
  const isAuth = useAppSelector((state) => state.auth.user)

  const logoutHandler = async () => {
    try {
      const response = await fetcher("http://localhost:8080/api/v1/auth/logout", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({}),
      });

      if (!response.ok) return console.log("Something went wrong", response);

      dispatch(logout())
      navigate("/");
    } catch (err) {
      console.log("You can't logout right now");
    }
  };

  return (
      <nav>
        <ul className={styles.navbar}>
          <li>
            <NavLink to="/statistics">Statistics</NavLink>
          </li>
          {!isAuth && (
            <li className={styles.login}>
              <NavLink to="/login">Login</NavLink>
            </li>
          )}
          {isAuth && (
            <li>
              <button onClick={logoutHandler}>Logout</button>
            </li>
          )}
        </ul>
      </nav>
  )
}

export default Navigation;
