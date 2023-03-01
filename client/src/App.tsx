import { createBrowserRouter, RouterProvider } from "react-router-dom";
import ChangePasswordPage from "./pages/ChangePassword";
import CompanyDetailPage from "./pages/CompanyDetail";
import ErrorPage from "./pages/Error";
import ForgotPasswordPage from "./pages/ForgotPassword";
import HomePage from "./pages/Home";
import JobDetailsPage from "./pages/JobDetails";
import JobsPage from "./pages/Jobs";
import JobsRoot from "./pages/JobsRoot";
import LoginPage from "./pages/Login";
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
      {
        path: "/jobs",
        element: <JobsRoot />,
        children: [
          { index: true, element: <JobsPage /> },
          {
            path: ":companyName",
            element: <CompanyDetailPage />,
          },
          {
            path: ":companyName/:jobTitle/:jobId",
            element: <JobDetailsPage />,
          },
        ],
      },
    ],
  },
]);

function App() {
  return <RouterProvider router={router} />;
}

export default App;
