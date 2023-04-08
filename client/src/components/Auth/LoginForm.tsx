import { Form, useNavigation } from "react-router-dom";

const LoginForm = () => {
  const navigation = useNavigation();
  const isSubmitting = navigation.state === "submitting";

  return (
    <>
      <Form method="post" action="/login">
        <div>
          <label htmlFor="email">Email</label>
          <input type="email" name="email" id="email" />
        </div>
        <div>
          <label htmlFor="password">Password</label>
          <input type="password" name="password" id="password" />
        </div>
        <button disabled={isSubmitting} type="submit">
          {isSubmitting ? "Loading..." : "Sing in"}
        </button>
      </Form>
    </>
  );
};

export default LoginForm;
