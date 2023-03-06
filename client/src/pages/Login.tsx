import { ActionFunctionArgs, json, Link, redirect } from "react-router-dom";
import LoginForm from "../components/LoginForm";

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
  const data = await request.formData();

  const credentials = {
    email: data.get("email"),
    password: data.get("password"),
  };

  const response = await fetch("http://localhost:8080/api/v1/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(credentials),
  });

  if (!response.ok)
    throw json({ message: "Couldn't singin, check your credentials" });

  return redirect("/registration");
}
