import jwtDecode from "jwt-decode";
import { ActionFunctionArgs, json, Link, redirect } from "react-router-dom";
import LoginForm from "../components/Auth/LoginForm";
import Card from "../components/UI/Card";
import { setCredentials } from "../store/auth";
import { store } from "../store/store";
import { ADMIN, CANDIDATE } from "../types/Roles";
import { User, UserJwtPayload } from "../types/User";
import styles from './Login.module.css';

function LoginPage() {
  return (
    <section className={styles.login}>
      <Card>
        <>
          <LoginForm />
          <Link to="/registration">Create an account</Link>
          <Link to="/forgot-password">Forgot password?</Link>
        </>
      </Card>
    </section>
  );
}

export default LoginPage;

export async function action({ request }: ActionFunctionArgs) {
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

    if (!response.ok) throw response;

    const content = await response.json();
    const payload = jwtDecode<UserJwtPayload>(content.token);

    const user: User = {
      role: payload.scope || "",
      email: payload.sub || "",
      firstName: payload.name || "",
    };

    store.dispatch(
      setCredentials({ isLoggedIn: true, user, token: content.token })
    );

    console.log(user.role);
    if (user.role === CANDIDATE) {
      return redirect("/user/configuration");
    }
    if (user.role === ADMIN) {
      return redirect("/admin/welcome");
    }

    return redirect("/login");
  } catch (err) {
    console.log(err);
    throw json({ message: "Error trying to authenticate" });
  }
}
