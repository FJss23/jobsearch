import { Navigate, Outlet, useLocation } from "react-router-dom";
import { useAppDisptach, useAppSelector } from "../../hooks/hooks";
import { logout } from "../../store/auth";

const RequireAuth = ({ role }: { role: string }) => {
  const isLoggedIn = useAppSelector((state) => state.auth.isLoggedIn);
  const dispatch = useAppDisptach();
  const user = useAppSelector((state) => state.auth.user);
  const location = useLocation();

  if (role !== user?.role) {
    dispatch(logout());
    throw new Error("An error occurred");
  }

  if (!isLoggedIn) {
    dispatch(logout());
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  return (
    <>
      <Outlet />
    </>
  );
};

export default RequireAuth;
