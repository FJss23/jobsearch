import { redirect } from "react-router-dom";
import { AuthContextType } from "../store/authContext";

export const checkAuth = (authCtx: AuthContextType) =>  {
  const { isLoggedIn } = authCtx;

  if (!isLoggedIn) return redirect("/login")
}
