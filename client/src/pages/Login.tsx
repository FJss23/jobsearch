import { ActionFunctionArgs, json, Link, redirect } from "react-router-dom";
import LoginForm from "../components/Auth/LoginForm";
import { authActions } from "../store/auth";
import { store } from "../store/store";

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

    if (response.ok) store.dispatch(authActions.login());
  } catch (err) {
    return json({ message: "Error trying to authenticate" });
  }
  return redirect("/jobs");
}
