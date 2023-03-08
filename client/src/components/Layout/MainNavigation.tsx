import { useDispatch } from "react-redux";
import { NavLink, useNavigate } from "react-router-dom";
import { useAppSelector } from "../../hooks/hooks";
import { authActions } from "../../store/auth";

function MainNavigation() {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const isAuth = useAppSelector((state) => state.auth.isAuthenticated);

  const logoutHandler = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/v1/auth/logout", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({})
      });
      console.log(response)

      if (response.ok) dispatch(authActions.logout());

      navigate("/");
    } catch (err) {
      console.log("You can't logout right now")
    }
  };

  return (
    <header>
      <nav>
        <ul>
          <li>
            <NavLink to="/">JobSearch</NavLink>
          </li>
          <li>
            <NavLink to="/jobs">Jobs</NavLink>
          </li>
          {!isAuth && (
            <li>
              <NavLink to="/login">Login</NavLink>
            </li>
          )}
          {isAuth && (
            <li>
              <NavLink to="/applications">Applications</NavLink>
            </li>
          )}
          {isAuth && <li><button onClick={logoutHandler}>Logout</button></li>}
        </ul>
      </nav>
    </header>
  );
}

export default MainNavigation;
