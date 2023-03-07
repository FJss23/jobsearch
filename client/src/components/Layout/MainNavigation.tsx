import { NavLink } from "react-router-dom";
import { useAppSelector } from "../../hooks/hooks";

function MainNavigation() {
  const isAuth = useAppSelector(state => state.auth.isAuthenticated);

  return (
    <header>
      <nav>
        <ul>
          <li><NavLink to="/">JobSearch</NavLink></li>
          <li><NavLink to="/jobs">Jobs</NavLink></li>
          {!isAuth && <li><NavLink to="/login">Login</NavLink></li>}
          {isAuth && <li><NavLink to="/applications">Applications</NavLink></li>}
          {isAuth && <li><NavLink to="/logout">Logout</NavLink></li>}
        </ul>
      </nav>
    </header>
  )
}

export default MainNavigation;
