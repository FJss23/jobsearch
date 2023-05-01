import { ActionFunctionArgs, json, redirect } from "react-router-dom";

function RegistrationPage() {
  return (
    <form>
      <div>
        <label htmlFor="firstName">First Name</label>
        <input type="text" name="firstName" id="fistName" />
      </div>
      <div>
        <label htmlFor="lastName">Last Name</label>
        <input type="text" name="lastName" id="lastName" />
      </div>
      <div>
        <label htmlFor="email">Email</label>
        <input type="email" name="email" id="email" />
      </div>
      <div>
        <label htmlFor="password">Password</label>
        <input type="password" name="password" id="password" />
      </div>
      <div>
        <label htmlFor="repeatPassword">Repeat Password</label>
        <input
          type="repeatPassword"
          name="repeatPassword"
          id="repeatPassword"
        />
      </div>
      <button type="submit">Sing up</button>
    </form>
  );
}

export default RegistrationPage;

export async function action({ request }: ActionFunctionArgs) {
  const formData = await request.formData();

  const registration = {
    firstName: formData.get("firstName") as string,
    lastName: formData.get("lastName") as string,
    email: formData.get("email") as string,
    password: formData.get("password") as string,
    repeatPassword: formData.get("repeatPassword") as string,
  };

  try {
    const response = await fetch("http://localhost:8080/api/v1/auth/registration", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(registration),
    });

    if (!response.ok) {
      return json({ message: "Error trying to authenticate" });
    }
    await response.json();
  } catch (err) {
    return json({ message: "Error trying to authenticate" });
  }
  return redirect("/login");
}
