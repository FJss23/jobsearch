import { ActionFunctionArgs, json, Link, redirect } from "react-router-dom";
import LoginForm from "../components/Auth/LoginForm";
import { User } from "../store/auth";
// import { store } from "../store/store";

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

    if (!response.ok) {
      return json({ message: "Error trying to authenticate" });
    }
    const data = await response.json();
    const user = data as User;
    console.log(user)
    // store.dispatch(authActions.login(user));
  } catch (err) {
    return json({ message: "Error trying to authenticate" });
  }
  return redirect("/jobs");
}
