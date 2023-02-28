import { NavLink } from "react-router-dom";

function MainNavigation() {
  return (
    <header>
      <nav>
        <ul>
          <li><NavLink to="/jobs">Jobs</NavLink></li>
          <li><NavLink to="/login">Login</NavLink></li>
          <li><NavLink to="/enterprise">Enterprise</NavLink></li>
        </ul>
      </nav>
    </header>
  )
}

export default MainNavigation;
