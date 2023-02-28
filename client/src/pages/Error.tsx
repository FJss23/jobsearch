import { useRouteError } from "react-router-dom";

type ErrorResponse = {
  status: number;
  statusText: string;
  internal: boolean;
  data: string;
  error: Error;
};

function ErrorPage() {
  const error = useRouteError() as ErrorResponse;

  let title = "An error occurred!";
  let message = "Something went wrong";

  if (error.status === 404) {
    title = "Not found!";
    message = "Couldn't find the page or the resource";
  }

  if (error.status === 500) {
    message = error.data;
  }

  return (
    <>
      <h1>{title}</h1>
      <p>{message}</p>
      <button onClick={() => (window.location.href = "/")}>
        Click here to reload the app
      </button>
    </>
  );
}

export default ErrorPage;
