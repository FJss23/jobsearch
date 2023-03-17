import { json, useLoaderData } from "react-router-dom";
import { Applications } from "../components/SelectionProcess/Applications";
import { useAppSelector } from "../hooks/hooks";

function ApplicationsPage() {
  const applications = useLoaderData() as Applications[];
  const isAuth = useAppSelector((state) => state.auth.user);

  return (
    <>
      {isAuth && (
        <ul>
          {applications.map((application) => (
            <li>
              <span>{application.companyId}</span>
              <span>{application.jobofferId}</span>
              <span>{application.discardedByCompany}</span>
              <span>{application.lastTimeReviewed.getTimezoneOffset()}</span>
            </li>
          ))}
        </ul>
      )}
    </>
  );
}

export default ApplicationsPage;

export async function loader() {
  const response = await fetch(
    "http://localhost:8080/api/v1/selection-process"
  );

  if (!response.ok)
    throw json({ message: "Couldn't get the information from the server" });

  let data = [];

  try {
    data = await response.json();
  } catch (error) {
    throw json({ message: "Couldn't convert the information from the server" });
  }

  return data;
}
