import { createAsyncThunk } from "@reduxjs/toolkit";
import { ActionFunctionArgs, Link, redirect } from "react-router-dom";
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
  const data = await request.formData();

  const credentials = {
    email: data.get("email") as string,
    password: data.get("password") as string,
  };

  await store.dispatch(login(credentials));

  return redirect("/registration");
}

