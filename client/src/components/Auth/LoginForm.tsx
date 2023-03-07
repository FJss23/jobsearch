import { Form } from "react-router-dom";

const LoginForm = () => {
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
        <button type="submit">Sing in</button>
      </Form>
    </>
  );
}

export default LoginForm;
