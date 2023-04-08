import { useContext } from "react";
import { Navigate, useLocation } from "react-router-dom";
import { AuthContext } from "../../store/authContext";

const RequireAuth = ({ children }: { children: JSX.Element }) => {
  const authCtx = useContext(AuthContext);
  const location = useLocation();

  if (!authCtx.isLoggedIn)
    return <Navigate to="login" state={{ from: location }} replace />;

  return children;
};

export default RequireAuth;
