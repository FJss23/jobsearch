import { Form, useNavigation } from "react-router-dom";
import Input from "../UI/Input";
import style from "./LoginForm.module.css";

const LoginForm = () => {
  const navigation = useNavigation();
  const isSubmitting = navigation.state === "submitting";

  return (
    <Form method="post" action="/login">
      <Input
        label="Email"
        input={{ id: "email", type: "email", name: "email", required: true }}
      />
      <Input
        label="Password"
        input={{
          id: "password",
          type: "password",
          name: "password",
          required: true,
        }}
      />
      <button
        disabled={isSubmitting}
        type="submit"
        className={style.loginButton}
      >
        {isSubmitting ? "Loading..." : "Sing in"}
      </button>
    </Form>
  );
};

export default LoginForm;
