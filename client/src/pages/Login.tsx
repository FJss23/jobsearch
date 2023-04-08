import jwtDecode from "jwt-decode";
import { ActionFunctionArgs, json, Link, redirect } from "react-router-dom";
import LoginForm from "../components/Auth/LoginForm";
import { AuthContextType } from "../store/authContext";
import { User, UserJwtPayload } from "../types/User";

function LoginPage() {
  return (
    <>
      <LoginForm />
      <Link to="/registration">Create an account</Link>
      <Link to="/forgot-password">Forgot password?</Link>
    </>
  );
}

export default LoginPage;

export const action =
  (authCtx: AuthContextType) =>
  async ({ request }: ActionFunctionArgs) => {
    const formData = await request.formData();

    const credentials = {
      email: formData.get("email") as string,
      password: formData.get("password") as string,
    };

    try {
      const response = await fetch("http://localhost:8080/api/v1/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(credentials),
      });

      if (!response.ok) {
        throw response;
      }

      const content = await response.json();
      const payload = jwtDecode<UserJwtPayload>(content.token);

      const user: User = {
        role: payload.scope || "",
        email: payload.sub || "",
        firstName: payload.name || "",
        accessToken: content.token,
      };

      authCtx.onLogin(user);
      redirect("/configuration");
    } catch (err) {
      throw json({ message: "Error trying to authenticate" });
    }
  };

