import { createBrowserRouter, RouterProvider } from "react-router-dom";
import ChangePasswordPage from "./pages/ChangePassword";
import CompanyDetailPage from "./pages/CompanyDetail";
import ErrorPage from "./pages/Error";
import ForgotPasswordPage from "./pages/ForgotPassword";
import HomePage from "./pages/Home";
import JobApplicationsPage from "./pages/JobApplications";
import JobDetailsPage from "./pages/JobDetails";
import JobsPage from "./pages/Jobs";
// import JobsRoot from "./pages/JobsRoot";
import LoginPage from "./pages/Login";
import NewCvPage from "./pages/NewCv";
import RegistrationPage from "./pages/Registration";
import RootLayout from "./pages/Root";

const router = createBrowserRouter([
  {
    path: "/",
    element: <RootLayout />,
    errorElement: <ErrorPage />,
    children: [
      { index: true, element: <HomePage /> },
      { path: "/registration", element: <RegistrationPage /> },
      { path: "/login", element: <LoginPage /> },
      { path: "/forgot-password", element: <ForgotPasswordPage /> },
      { path: "/change-password", element: <ChangePasswordPage /> },
      { path: "/jobs", element: <JobsPage /> },
      {
        path: "/jobs/:companyName/:companyId/:jobTitle/:jobId",
        element: <JobDetailsPage />,
      },
      {
        path: "/company/:companyName/:companyId",
        element: <CompanyDetailPage />,
      },
      { path: "/cv/new", element: <NewCvPage /> },
      { path: "/applications", element: <JobApplicationsPage /> },
    ],
  },
]);

function App() {
  return <RouterProvider router={router} />;
}

export default App;
